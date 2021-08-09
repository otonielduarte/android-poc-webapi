package br.com.alura.stock.ui.recyclerview.adapter

import android.content.Context
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.stock.R
import br.com.alura.stock.model.Product
import java.math.BigDecimal
import java.text.NumberFormat
import kotlin.collections.ArrayList

class ProductListAdapter(
    private val context: Context,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private lateinit var onItemClickRemoveContextMenuListener: OnItemClickRemoveContextMenuListener
    private val products: ArrayList<Product> = ArrayList()

    fun setOnItemClickRemoveContextMenuListener(onItemClickRemoveContextMenuListener: OnItemClickRemoveContextMenuListener) {
        this.onItemClickRemoveContextMenuListener = onItemClickRemoveContextMenuListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val createdView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(createdView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun update(products: List<Product>) {
        notifyItemRangeRemoved(0, this.products.size)
        this.products.clear()
        this.products.addAll(products)
        notifyItemRangeInserted(0, this.products.size)
    }

    fun addProduct(products: Product) {
        val size = this.products.size
        this.products.add(products)
        val newSize = this.products.size
        notifyItemRangeInserted(size, newSize)
    }

    fun edit(position: Int, product: Product) {
        products[position] = product
        notifyItemChanged(position)
    }

    fun remove(posicao: Int) {
        products.removeAt(posicao)
        notifyItemRemoved(posicao)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fieldId: TextView
        private val fieldName: TextView
        private val fieldPrice: TextView
        private val fieldQuantity: TextView
        private lateinit var product: Product
        private fun handleContextMenu(itemView: View) {
            itemView.setOnCreateContextMenuListener { menu: ContextMenu, v: View?, menuInfo: ContextMenuInfo? ->
                MenuInflater(
                    context).inflate(R.menu.product_list_menu, menu)

                menu.findItem(R.id.product_list_remove_menu)
                    .setOnMenuItemClickListener { item: MenuItem? ->
                        val productPosition = adapterPosition
                        onItemClickRemoveContextMenuListener
                            .onItemClick(productPosition, product)
                        true
                    }
            }
        }

        private fun handleClick(itemView: View) {
            itemView.setOnClickListener {
                onItemClickListener
                    .onItemClick(adapterPosition, product)
            }
        }

        fun bind(product: Product) {
            this.product = product
            fieldId.text = product.id.toString()
            fieldName.text = product.name
            fieldPrice.text = toCurrency(product.getPrice())
            fieldQuantity.text = product.quantity.toString()
        }

        private fun toCurrency(valor: BigDecimal): String {
            val formatter = NumberFormat.getCurrencyInstance()
            return formatter.format(valor)
        }

        init {
            fieldId = itemView.findViewById(R.id.item_product_id)
            fieldName = itemView.findViewById(R.id.item_product_name)
            fieldPrice = itemView.findViewById(R.id.item_product_price)
            fieldQuantity = itemView.findViewById(R.id.item_product_quantity)
            handleClick(itemView)
            handleContextMenu(itemView)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, product: Product)
    }

    interface OnItemClickRemoveContextMenuListener {
        fun onItemClick(position: Int, removedProduct: Product)
    }
}