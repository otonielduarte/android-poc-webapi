package br.com.alura.stock.repository

import android.content.Context
import br.com.alura.stock.database.StockDatabase
import br.com.alura.stock.database.dao.ProductDao
import br.com.alura.stock.model.Product
import br.com.alura.stock.retrofit.callback.BaseCallback
import br.com.alura.stock.retrofit.callback.VoidCallback
import br.com.alura.stock.retrofit.service.IProductService
import br.com.alura.stock.retrofit.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductRepository(context: Context, private val listener: RepositoryCallbacks<Product>) {

    private val dao: ProductDao
    private val productService: IProductService

    init {
        val db: StockDatabase = StockDatabase.getInstance(context)
        dao = db.productDAO!!
        productService = ProductService.instance()
    }

    fun searchProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            val products: List<Product> = dao.all
            listener.afterLoaded(products)
            withContext(Dispatchers.Main) {
                searchFromApi()
            }
        }
    }

    private fun searchFromApi() {
        val callback = productService.getAll()
        callback.enqueue(BaseCallback(object : BaseCallback.ResponseListener<List<Product>> {
            override fun onSuccess(result: List<Product>) {
                updateOnDB(result)
            }

            override fun onFailure(result: String) {
                listener.onFailure(result)
            }
        }))
    }

    private fun updateOnDB(productResponse: List<Product>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.saveAll(productResponse)
            val products = dao.all
            withContext(Dispatchers.Main) {
                listener.afterLoaded(products)
            }
        }
    }

    fun remove(
        position: Int,
        removedProduct: Product,
    ) {
        val callback = productService.delete(removedProduct.id)
        callback.enqueue(VoidCallback(object : VoidCallback.ResponseListener {
            override fun onSuccess() {
                listener.whenRemoved(position)
                CoroutineScope(Dispatchers.IO).launch {
                    dao.delete(removedProduct)
                }
            }
            override fun onFailure(message: String) {
                listener.onFailure(message)
            }
        }))
    }

    fun save(product: Product) {
        val callback = productService.save(product)
        callback.enqueue(BaseCallback(object : BaseCallback.ResponseListener<Product> {
            override fun onSuccess(result: Product) {
                listener.whenAdded(result)
                CoroutineScope(Dispatchers.IO).launch {
                    dao.save(result)
                }
            }

            override fun onFailure(result: String) {
                listener.onFailure(result)
            }
        }))
    }

    fun edit(position: Int, editedProduct: Product) {
        val callback = productService.update(editedProduct.id, editedProduct)
        callback.enqueue(BaseCallback(object : BaseCallback.ResponseListener<Product> {
            override fun onSuccess(result: Product) {
                listener.whenChanged(position, result)
                CoroutineScope(Dispatchers.IO).launch {
                    dao.update(editedProduct)
                }
            }

            override fun onFailure(result: String) {
                listener.onFailure(result)
            }
        }))

    }

    interface RepositoryCallbacks<T> {
        fun afterLoaded(result: List<T>)
        fun whenRemoved(position: Int)
        fun whenChanged(position: Int, product: T)
        fun whenAdded(result: T)
        fun onFailure(message: String)
    }
}