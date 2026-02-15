package mx.ubam.inventarioapp.ui.client

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import mx.ubam.inventarioapp.data.CartStore
import mx.ubam.inventarioapp.data.Prefs
import mx.ubam.inventarioapp.data.net.ApiClient
import mx.ubam.inventarioapp.databinding.ActivityClientProductsBinding
import kotlinx.coroutines.launch

class ClientProductsActivity : AppCompatActivity() {

    private lateinit var b: ActivityClientProductsBinding
    private lateinit var prefs: Prefs
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityClientProductsBinding.inflate(layoutInflater)
        setContentView(b.root)

        prefs = Prefs(this)

        adapter = ProductAdapter(onAdd = { p ->
            // ✅ Agregar al carrito
            CartStore.add(p)
            Toast.makeText(this, "✅ Agregado al carrito: ${p.name}", Toast.LENGTH_SHORT).show()

            // (Opcional) si tuvieras un badge/contador, aquí lo actualizas.
            // updateCartBadge()
        })

        b.rvProducts.layoutManager = LinearLayoutManager(this)
        b.rvProducts.adapter = adapter

        b.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        load()
    }

    override fun onResume() {
        super.onResume()
        // ✅ No borra carrito, solo recarga lista de productos
        load()
        // updateCartBadge() // si lo implementas
    }

    private fun load() {
        val api = ApiClient.create(prefs)
        lifecycleScope.launch {
            try {
                val products = api.getProducts()
                adapter.submit(products)
            } catch (e: Exception) {
                adapter.submit(emptyList())
                Toast.makeText(this@ClientProductsActivity, "Error al cargar productos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Si luego quieres contador en el botón carrito:
    // private fun updateCartBadge() {
    //     val count = CartStore.snapshot().sumOf { it.second }
    //     b.btnCart.text = "Carrito ($count)"
    // }
}
