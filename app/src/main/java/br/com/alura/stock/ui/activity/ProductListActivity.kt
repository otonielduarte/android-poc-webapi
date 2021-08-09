package br.com.alura.stock.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.stock.R
import br.com.alura.stock.model.Product
import br.com.alura.stock.repository.ProductRepository
import br.com.alura.stock.ui.dialog.EditProductDialog
import br.com.alura.stock.ui.dialog.FormProductDialog
import br.com.alura.stock.ui.dialog.SaveProductDialog
import br.com.alura.stock.ui.recyclerview.adapter.ProductListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductListActivity : AppCompatActivity(), ProductRepository.RepositoryCallbacks<Product> {
    private val APPBAR_TITLE = "Product List"
    private lateinit var adapter: ProductListAdapter
    private lateinit var repository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        title = APPBAR_TITLE
        ProductListConfig()
        ConfigSaveProductFab()

        repository = ProductRepository(this, this)
        repository.searchProducts()
    }


    private fun ProductListConfig() {
        val productList = findViewById<RecyclerView>(R.id.activity_product_list)
        createAdapter()
        productList.adapter = adapter
        adapter.setOnItemClickRemoveContextMenuListener(object :
            ProductListAdapter.OnItemClickRemoveContextMenuListener {
            override fun onItemClick(position: Int, removedProduct: Product) {
                repository.remove(
                    position,
                    removedProduct
                )
            }
        })
    }

    private fun createAdapter() {
        adapter = ProductListAdapter(this, object : ProductListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, product: Product) {
                openFormProductEdit(position, product)
            }
        })
    }

    private fun ConfigSaveProductFab() {
        val fabProductAdd = findViewById<FloatingActionButton>(R.id.fabAddProduct)
        fabProductAdd.setOnClickListener { v: View? -> openFormProductSave() }
    }

    private fun openFormProductSave() {
        SaveProductDialog(this, (object :
            FormProductDialog.confirmListener {
            override fun afterConfirmed(product: Product) {
                repository.save(product)
            }
        })).show()
    }

    private fun openFormProductEdit(position: Int, product: Product) {
        EditProductDialog(this, product, object : FormProductDialog.confirmListener {
            override fun afterConfirmed(product: Product) {
                repository.edit(position, product)
            }
        }).show()
    }

    override fun afterLoaded(products: List<Product>) {
        adapter.update(products)
    }

    override fun whenRemoved(position: Int) {
        adapter.remove(position)
    }

    override fun whenChanged(position: Int, product: Product) {
        adapter.edit(position, product)
    }

    override fun whenAdded(product: Product) {
        adapter.addProduct(product)
    }

    override fun onFailure(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }
}