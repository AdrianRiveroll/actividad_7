package mx.ubam.inventarioapp.ui.sales

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import mx.ubam.inventarioapp.data.Prefs
import mx.ubam.inventarioapp.data.net.ApiClient
import mx.ubam.inventarioapp.databinding.ActivitySaleDetailBinding
import kotlinx.coroutines.launch

class SaleDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivitySaleDetailBinding
    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySaleDetailBinding.inflate(layoutInflater)
        setContentView(b.root)

        prefs = Prefs(this)
        val id = intent.getLongExtra("saleId", -1L)
        if (id <= 0) { finish(); return }
        load(id)
    }

    private fun load(id: Long) {
        val api = ApiClient.create(prefs)
        lifecycleScope.launch {
            try {
                val sale = api.getSaleDetail(id)
                val sb = StringBuilder()
                sb.append("Venta #").append(sale.id).append("\n")
                sb.append("Cliente: ").append(sale.customerName ?: "").append("\n")
                sb.append("Fecha: ").append(sale.createdAt ?: "").append("\n")
                sb.append("Total: ").append(sale.total ?: 0.0).append("\n\n")
                sb.append("Items:\n")
                sale.items?.forEach {
                    sb.append("- ").append(it.product?.name ?: "Producto")
                        .append(" x").append(it.quantity)
                        .append(" = ").append(it.lineTotal ?: 0.0)
                        .append("\n")
                }
                b.tvBody.text = sb.toString()
            } catch (e: Exception) {
                Toast.makeText(this@SaleDetailActivity, "⚠️ GET /api/sales/<built-in function id> falló: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
