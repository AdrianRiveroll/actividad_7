package mx.ubam.inventarioapp.ui.client

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import mx.ubam.inventarioapp.data.CartStore
import mx.ubam.inventarioapp.data.Prefs
import mx.ubam.inventarioapp.data.model.CreateSaleRequest
import mx.ubam.inventarioapp.data.model.SaleItemRequest
import mx.ubam.inventarioapp.data.net.ApiClient
import mx.ubam.inventarioapp.databinding.ActivityCartBinding
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var b: ActivityCartBinding
    private lateinit var prefs: Prefs
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCartBinding.inflate(layoutInflater)
        setContentView(b.root)

        prefs = Prefs(this)

        adapter = CartAdapter(
            onPlus = { id -> CartStore.plus(id); refresh() },
            onMinus = { id -> CartStore.minus(id); refresh() },
            onRemove = { id -> CartStore.remove(id); refresh() }
        )

        b.rvCart.layoutManager = LinearLayoutManager(this)
        b.rvCart.adapter = adapter

        b.btnCheckout.setOnClickListener { checkout() }
        refresh()
    }

    private fun refresh() {
        val list = CartStore.snapshot()
        adapter.submit(list)

        b.btnCheckout.isEnabled = list.isNotEmpty()
        b.tvTotal.text = "Total: " + String.format("%.2f", CartStore.total())
    }

    private fun checkout() {
        val list = CartStore.snapshot()
        if (list.isEmpty()) {
            Toast.makeText(this, "Tu carrito está vacío.", Toast.LENGTH_SHORT).show()
            return
        }

        val api = ApiClient.create(prefs)
        val username = prefs.getUsername() ?: "cliente"

        val items = CartStore.toSaleItems()
            .map { (pid, qty) -> SaleItemRequest(pid, qty) }

        val req = CreateSaleRequest(customerName = username, items = items)

        lifecycleScope.launch {
            try {
                api.createSale(req)
                CartStore.clear()
                Toast.makeText(this@CartActivity, "✅ Compra registrada", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@CartActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
