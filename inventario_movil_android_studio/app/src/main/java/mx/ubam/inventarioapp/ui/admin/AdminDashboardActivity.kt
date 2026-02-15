package mx.ubam.inventarioapp.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mx.ubam.inventarioapp.data.Prefs
import mx.ubam.inventarioapp.ui.LoginActivity
import mx.ubam.inventarioapp.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var b: ActivityAdminDashboardBinding
    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(b.root)

        prefs = Prefs(this)

        b.btnProducts.setOnClickListener { startActivity(Intent(this, AdminProductsActivity::class.java)) }
        b.btnSales.setOnClickListener { startActivity(Intent(this, AdminSalesActivity::class.java)) }
        b.btnLogout.setOnClickListener {
            prefs.clear()
            val i = Intent(this, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }
    }
}
