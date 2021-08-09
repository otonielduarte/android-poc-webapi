package br.com.alura.stock.retrofit.service


import br.com.alura.stock.model.Product
import retrofit2.Call
import retrofit2.http.*

interface IProductService {

    @GET("produto")
    fun getAll(): Call<List<Product>>

    @POST("produto")
    fun save(@Body product: Product): Call<Product>

    @PUT("produto/{id}")
    fun update(
        @Path("id") id: Long,
        @Body product: Product,
    ): Call<Product>

    @DELETE("produto/{id}")
    fun delete(@Path("id") id: Long): Call<Void>
}