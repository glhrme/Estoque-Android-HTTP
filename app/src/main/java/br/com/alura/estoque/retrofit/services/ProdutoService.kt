package br.com.alura.estoque.retrofit.services

import br.com.alura.estoque.model.Produto
import retrofit2.Call
import retrofit2.http.GET

interface ProdutoService {
    @GET("produto")
    fun buscaTodos(): Call<List<Produto>>;
}