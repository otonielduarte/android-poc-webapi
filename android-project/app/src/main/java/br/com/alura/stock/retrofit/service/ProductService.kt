package br.com.alura.stock.retrofit.service

import android.content.Context
import br.com.alura.stock.retrofit.StockRetrofit

class ProductService {
    companion object {
        fun instance(): IProductService {
            return StockRetrofit.getInstance().create(IProductService::class.java)
        }
    }
}