package br.com.alura.stock.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alura.stock.database.converter.BigDecimalConverter
import br.com.alura.stock.database.dao.ProductDao
import br.com.alura.stock.model.Product

@Database(entities = [Product::class], version = 2, exportSchema = false)
@TypeConverters(value = [BigDecimalConverter::class])
abstract class StockDatabase : RoomDatabase() {
    abstract val productDAO: ProductDao?

    companion object {
        private const val DB_NAME = "stock.db"
        fun getInstance(context: Context?): StockDatabase {
            return Room.databaseBuilder(
                context!!,
                StockDatabase::class.java,
                DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}