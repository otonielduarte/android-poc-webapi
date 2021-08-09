package br.com.alura.stock.ui.dialog

import android.content.Context
import br.com.alura.stock.model.Product

class EditProductDialog(
    context: Context?,
    product: Product?,
    listener: confirmListener
) : FormProductDialog(context, TITLE, TITLE_BTN_ADD, listener, product) {
    companion object {
        private const val TITLE = "Edit product"
        private const val TITLE_BTN_ADD = "Edit"
    }
}