package br.com.alura.estoque.retrofit.services

import br.com.alura.estoque.model.Produto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProdutoService {
    @GET("produto")
    fun buscaTodos(): Call<List<Produto>>

    @POST("produto")
    fun salva(@Body produto: Produto): Call<Produto>
}