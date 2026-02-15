package mx.ubam.inventarioapp.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import mx.ubam.inventarioapp.data.Prefs
import mx.ubam.inventarioapp.data.net.ApiClient
import mx.ubam.inventarioapp.databinding.ActivityAdminSalesBinding
import mx.ubam.inventarioapp.ui.sales.SaleDetailActivity
import kotlinx.coroutines.launch

class AdminSalesActivity : AppCompatActivity() {
    private lateinit var b: ActivityAdminSalesBinding
    private lateinit var prefs: Prefs
    private lateinit var adapter: SalesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAdminSalesBinding.inflate(layoutInflater)
        setContentView(b.root)

        prefs = Prefs(this)
        adapter = SalesAdapter(onView = { id ->
            startActivity(Intent(this, SaleDetailActivity::class.java).putExtra("saleId", id))
        })

        b.rvSales.layoutManager = LinearLayoutManager(this)
        b.rvSales.adapter = adapter

        load()
    }

    private fun load() {
        val api = ApiClient.create(prefs)
        lifecycleScope.launch {
            try {
                adapter.submit(api.getSales())
            } catch (e: Exception) {
                Toast.makeText(this@AdminSalesActivity, "⚠️ GET /api/sales no existe o falló: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
