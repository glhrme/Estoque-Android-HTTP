package br.com.alura.estoque.retrofit

import android.util.Log
import br.com.alura.estoque.retrofit.services.ProdutoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class EstoqueRetrofit() {
    var retrofit: Retrofit? = null
    var retroCreated: ProdutoService? = null

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://192.168.0.198:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retroCreated = retrofit!!.create(ProdutoService::class.java)
    }

    fun getProdutoService(): ProdutoService {
        Log.i("getProdutoService", "getProdutoService")
        return retroCreated!!
    }
}