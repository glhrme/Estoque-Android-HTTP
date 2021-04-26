package br.com.alura.estoque.repository

import android.os.AsyncTask
import br.com.alura.estoque.asynctask.BaseAsyncTask
import br.com.alura.estoque.asynctask.BaseAsyncTask.ExecutaListener
import br.com.alura.estoque.asynctask.BaseAsyncTask.FinalizadaListener
import br.com.alura.estoque.database.dao.ProdutoDAO
import br.com.alura.estoque.model.Produto
import br.com.alura.estoque.retrofit.EstoqueRetrofit
import br.com.alura.estoque.ui.recyclerview.adapter.ListaProdutosAdapter
import retrofit2.Call
import java.io.IOException

class ProdutoRepository(private val dao: ProdutoDAO) {
    fun buscaProdutos(listener: ProdutosCarregadosListener) {
        buscaProdutosInternos(EstoqueRetrofit().getProdutoService().buscaTodos(), listener)
    }

    private fun buscaProdutosInternos(call: Call<List<Produto>>, listener: ProdutosCarregadosListener) {
        BaseAsyncTask({ dao.buscaTodos() }, { resultado: List<Produto> ->
            listener.onCarregados(resultado)
            buscaProdutosNaApi(call, listener)
        }).execute()
    }

    private fun buscaProdutosNaApi(call: Call<List<Produto>>, listener: ProdutosCarregadosListener) {
        BaseAsyncTask({
            try {
                val response = call.execute()
                val produtosNovos = response.body()
                if (produtosNovos != null) dao.salva(produtosNovos)
            } catch (err: IOException) {
                err.printStackTrace()
            }
            dao.buscaTodos()
        }, { produtosNovos: List<Produto> -> listener.onCarregados(produtosNovos)})
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    interface ProdutosCarregadosListener {
        fun onCarregados(produtos: List<Produto>)
    }
}