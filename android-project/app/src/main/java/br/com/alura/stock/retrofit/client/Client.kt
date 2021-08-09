package br.com.alura.stock.retrofit.client

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class Client {
    companion object {
        fun getInstance(): OkHttpClient {
            val logging = HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        }
    }
}