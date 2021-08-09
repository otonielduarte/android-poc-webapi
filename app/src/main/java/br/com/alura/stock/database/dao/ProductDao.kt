package br.com.alura.stock.database.dao

import androidx.room.*
import br.com.alura.stock.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(products: List<Product>)

    @Update
    fun update(product: Product)

    @get:Query("SELECT * FROM Product")
    val all: List<Product>

    @Query("SELECT * FROM Product WHERE id = :id")
    fun searchProduct(id: Long): Product?

    @Delete
    fun delete(product: Product)
}