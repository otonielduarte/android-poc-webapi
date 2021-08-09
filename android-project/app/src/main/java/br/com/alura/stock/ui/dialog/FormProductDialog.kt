package br.com.alura.stock.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import br.com.alura.stock.R
import br.com.alura.stock.model.Product
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal

abstract class FormProductDialog internal constructor(
    private val context: Context?,
    private val title: String?,
    private val titleBtn: String?,
    private val listener: confirmListener
) {
    private var product: Product? = null

    internal constructor(
        context: Context?,
        title: String?,
        titleBtn: String?,
        listener: confirmListener,
        product: Product?
    ) : this(context, title, titleBtn, listener) {
        this.product = product
    }

    fun show() {
        @SuppressLint("InflateParams") val view = LayoutInflater.from(context)
            .inflate(R.layout.formulario_produto, null)
        tryBindForm(view)
        AlertDialog.Builder(context!!)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(titleBtn) { dialog: DialogInterface?, which: Int ->
                val fieldName = getEditText(view, R.id.form_product_name)
                val fieldPrice = getEditText(view, R.id.form_product_price)
                val fieldQuantity = getEditText(view, R.id.form_product_quantity)
                createProduct(fieldName, fieldPrice, fieldQuantity)
            }
            .setNegativeButton(TITLE_BTN_CANCEL, null)
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun tryBindForm(view: View) {
        if (product != null) {
            val fieldId = view.findViewById<TextView>(R.id.form_product_id)
            fieldId.text = product!!.id.toString()
            fieldId.visibility = View.VISIBLE
            val fieldName = getEditText(view, R.id.form_product_name)
            fieldName!!.setText(product!!.name)
            val fieldPrice = getEditText(view, R.id.form_product_price)
            fieldPrice!!.setText(product!!.getPrice().toString())
            val fieldQuantity = getEditText(view, R.id.form_product_quantity)
            fieldQuantity!!.setText(product!!.quantity.toString())
        }
    }

    private fun createProduct(
        fieldName: EditText?,
        fieldPrice: EditText?,
        fieldQuantity: EditText?
    ) {
        val name = fieldName!!.text.toString()
        val price = convertPrice(fieldPrice)
        val quantity = convertQuantity(fieldQuantity)
        val id = id
        val product = Product(id, name, price, quantity)
        listener.afterConfirmed(product)
    }

    private val id: Long
        private get() = if (product != null) {
            product!!.id
        } else 0

    private fun convertPrice(campoPreco: EditText?): BigDecimal {
        return try {
            BigDecimal(campoPreco!!.text.toString())
        } catch (ignored: NumberFormatException) {
            BigDecimal.ZERO
        }
    }

    private fun convertQuantity(fieldQuantity: EditText?): Int {
        return try {
            fieldQuantity!!.text.toString().toInt()
        } catch (ignored: NumberFormatException) {
            0
        }
    }

    private fun getEditText(view: View, idTextInputLayout: Int): EditText? {
        val textInputLayout: TextInputLayout = view.findViewById(idTextInputLayout)
        return textInputLayout.editText
    }

    interface confirmListener {
        fun afterConfirmed(product: Product)
    }

    companion object {
        private const val TITLE_BTN_CANCEL = "Cancel"
    }
}