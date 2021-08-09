package br.com.alura.stock.ui.dialog

import android.content.Context

class SaveProductDialog(
    context: Context?,
    listener: confirmListener
) : FormProductDialog(context, TITULO, TITULO_BOTAO_POSITIVO, listener) {
    companion object {
        private const val TITULO = "Salvando produto"
        private const val TITULO_BOTAO_POSITIVO = "Salvar"
    }
}