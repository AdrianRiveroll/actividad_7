package mx.ubam.inventarioapp.ui.admin

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import mx.ubam.inventarioapp.data.model.Product
import mx.ubam.inventarioapp.databinding.DialogProductBinding

class ProductDialog(
    context: Context,
    product: Product?,
    private val onSave: (Map<String, Any?>) -> Unit
) {
    private val b = DialogProductBinding.inflate(LayoutInflater.from(context))

    init {
        b.etName.setText(product?.name ?: "")
        b.etBrand.setText(product?.brand ?: "")
        b.etCap.setText(product?.storageCapacity ?: "")
        b.etStock.setText(product?.stock?.toString() ?: "0")
        b.etPrice.setText(product?.price?.toString() ?: "0")
    }

    private val dialog = AlertDialog.Builder(context)
        .setTitle(if (product == null) "Agregar producto" else "Editar producto")
        .setView(b.root)
        .setPositiveButton("Guardar") { _, _ ->
            val payload = mapOf(
                "name" to b.etName.text?.toString()?.trim().orEmpty(),
                "brand" to b.etBrand.text?.toString()?.trim().orEmpty(),
                "storageCapacity" to b.etCap.text?.toString()?.trim().orEmpty(),
                "stock" to (b.etStock.text?.toString()?.trim()?.toIntOrNull() ?: 0),
                "price" to (b.etPrice.text?.toString()?.trim()?.toDoubleOrNull() ?: 0.0)
            )
            onSave(payload)
        }
        .setNegativeButton("Cancelar", null)
        .create()

    fun show() = dialog.show()
}
