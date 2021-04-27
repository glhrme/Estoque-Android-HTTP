package br.com.alura.estoque.repository

import android.os.AsyncTask
import br.com.alura.estoque.asynctask.BaseAsyncTask
import br.com.alura.estoque.database.dao.ProdutoDAO
import br.com.alura.estoque.model.Produto
import br.com.alura.estoque.retrofit.EstoqueRetrofit
import br.com.alura.estoque.retrofit.services.ProdutoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ProdutoRepository(private val dao: ProdutoDAO) {

    private val service: ProdutoService = EstoqueRetrofit().getProdutoService()

    fun buscaProdutos(listener: DadosCarregadosListener<List<Produto>>) {
        buscaProdutosInternos(service.buscaTodos(), listener)
    }

    private fun buscaProdutosInternos(call: Call<List<Produto>>, listener: DadosCarregadosListener<List<Produto>>) {
        BaseAsyncTask({ dao.buscaTodos() }, { resultado: List<Produto> ->
            listener.quandoCarregados(resultado)
            buscaProdutosNaApi(call, listener)
        }).execute()
    }

    private fun buscaProdutosNaApi(call: Call<List<Produto>>, listener: DadosCarregadosListener<List<Produto>>) {
        BaseAsyncTask({
            try {
                val response = call.execute()
                val produtosNovos = response.body()
                if (produtosNovos != null) dao.salva(produtosNovos)
            } catch (err: IOException) {
                err.printStackTrace()
            }
            dao.buscaTodos()
        }, { produtosNovos: List<Produto> -> listener.quandoCarregados(produtosNovos) })
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    fun salva(produto: Produto, listener: DadosCarregadosListener<Produto>) {
        val call = service.salva(produto)
        call.enqueue(object : Callback<Produto> {
            override fun onResponse(call: Call<Produto>, response: Response<Produto>) {
                val produtoSalvo: Produto = response.body()!!
                BaseAsyncTask({
                    val id = dao.salva(produtoSalvo)
                    dao.buscaProduto(id)
                }) { p: Produto ->
                    listener.quandoCarregados(p)
                }.execute()
            }

            override fun onFailure(call: Call<Produto>,
                                   t: Throwable) {
            }
        })

    }

    interface ProdutosCarregadosListener {
        fun onCarregados(produtos: List<Produto>)

        fun onSalvos()
    }

    interface DadosCarregadosListener<T> {
        fun quandoCarregados(resultado: T)
    }
}