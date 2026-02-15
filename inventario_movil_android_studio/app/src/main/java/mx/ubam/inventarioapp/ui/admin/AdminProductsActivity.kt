package mx.ubam.inventarioapp.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import mx.ubam.inventarioapp.data.Prefs
import mx.ubam.inventarioapp.data.model.Product
import mx.ubam.inventarioapp.data.net.ApiClient
import mx.ubam.inventarioapp.databinding.ActivityAdminProductsBinding
import kotlinx.coroutines.launch

class AdminProductsActivity : AppCompatActivity() {
    private lateinit var b: ActivityAdminProductsBinding
    private lateinit var prefs: Prefs
    private lateinit var adapter: AdminProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAdminProductsBinding.inflate(layoutInflater)
        setContentView(b.root)

        prefs = Prefs(this)
        adapter = AdminProductAdapter(onEdit = { p -> edit(p) }, onDelete = { p -> delete(p) })

        b.rvProducts.layoutManager = LinearLayoutManager(this)
        b.rvProducts.adapter = adapter

        b.btnAdd.setOnClickListener { create() }
        load()
    }

    private fun load() {
        val api = ApiClient.create(prefs)
        lifecycleScope.launch {
            try {
                adapter.submit(api.getProducts())
            } catch (e: Exception) {
                Toast.makeText(this@AdminProductsActivity, "No se pudieron cargar productos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun create() {
        ProductDialog(this, null) { payload ->
            val api = ApiClient.create(prefs)
            lifecycleScope.launch {
                try {
                    api.createProduct(payload)
                    Toast.makeText(this@AdminProductsActivity, "✅ Producto creado", Toast.LENGTH_SHORT).show()
                    load()
                } catch (e: Exception) {
                    Toast.makeText(this@AdminProductsActivity, "⚠️ POST /api/products no existe o falló: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.show()
    }

    private fun edit(p: Product) {
        ProductDialog(this, p) { payload ->
            val api = ApiClient.create(prefs)
            lifecycleScope.launch {
                try {
                    api.updateProduct(p.id, payload)
                    Toast.makeText(this@AdminProductsActivity, "✅ Actualizado", Toast.LENGTH_SHORT).show()
                    load()
                } catch (e: Exception) {
                    Toast.makeText(this@AdminProductsActivity, "⚠️ PUT /api/products/<built-in function id> no existe o falló: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.show()
    }

    private fun delete(p: Product) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar '${p.name}'? (mantén presionado un producto para eliminar)")
            .setPositiveButton("Sí") { _, _ ->
                val api = ApiClient.create(prefs)
                lifecycleScope.launch {
                    try {
                        api.deleteProduct(p.id)
                        Toast.makeText(this@AdminProductsActivity, "✅ Eliminado", Toast.LENGTH_SHORT).show()
                        load()
                    } catch (e: Exception) {
                        Toast.makeText(this@AdminProductsActivity, "⚠️ DELETE /api/products/<built-in function id> no existe o falló: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
