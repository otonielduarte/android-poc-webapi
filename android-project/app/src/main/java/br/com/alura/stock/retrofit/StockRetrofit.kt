package br.com.alura.stock.retrofit

import br.com.alura.stock.retrofit.client.Client
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StockRetrofit {
    companion object {
        private const val baseURL = "http://192.168.0.108:8080/"
        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(Client.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}