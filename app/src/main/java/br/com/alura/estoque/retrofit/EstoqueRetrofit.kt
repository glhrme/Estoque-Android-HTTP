package br.com.alura.estoque.retrofit

import br.com.alura.estoque.retrofit.services.ProdutoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EstoqueRetrofit() {
    var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.198:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun getProdutoService(): ProdutoService {
        return retrofit!!.create(ProdutoService::class.java)
    }
}