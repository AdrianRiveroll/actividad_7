package mx.ubam.inventarioapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import mx.ubam.inventarioapp.data.Prefs
import mx.ubam.inventarioapp.data.model.LoginRequest
import mx.ubam.inventarioapp.data.net.ApiClient
import mx.ubam.inventarioapp.util.JwtUtils
import mx.ubam.inventarioapp.ui.admin.AdminDashboardActivity
import mx.ubam.inventarioapp.ui.client.ClientProductsActivity
import mx.ubam.inventarioapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var b: ActivityLoginBinding
    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        prefs = Prefs(this)


        val token = prefs.getToken()
        val role = prefs.getRole()
        if (!token.isNullOrBlank() && !role.isNullOrBlank()) {
            goByRole(role)
            finish()
            return
        }


        setRoleDefaults(isAdmin = false)


        b.rgRole.setOnCheckedChangeListener { _, checkedId ->
            val isAdmin = checkedId == b.rbAdmin.id
            setRoleDefaults(isAdmin)
        }

        b.btnLogin.setOnClickListener {
            val user = b.etUser.text?.toString()?.trim().orEmpty()
            val pass = b.etPass.text?.toString()?.trim().orEmpty()
            if (user.isBlank() || pass.isBlank()) {
                showError("Completa usuario y contraseña.")
                return@setOnClickListener
            }

            val isAdminSelected = b.rgRole.checkedRadioButtonId == b.rbAdmin.id
            doLogin(user, pass, isAdminSelected)
        }
    }

    private fun setRoleDefaults(isAdmin: Boolean) {
        if (isAdmin) {
            b.rbAdmin.isChecked = true
            b.etUser.setText("admin")
            b.etPass.setText("admin123")
        } else {
            b.rbCliente.isChecked = true
            b.etUser.setText("cliente")
            b.etPass.setText("client123")
        }
    }

    private fun doLogin(user: String, pass: String, isAdminSelected: Boolean) {
        b.progress.visibility = View.VISIBLE
        b.tvError.visibility = View.GONE

        val api = ApiClient.create(prefs)

        lifecycleScope.launch {
            try {
                val resp = api.login(LoginRequest(user, pass))
                val token = resp.token ?: throw IllegalStateException("El backend no devolvió token.")


                val roleFromBackendOrJwt = resp.role ?: JwtUtils.roleFromToken(token)

                prefs.saveToken(token)
                prefs.saveUsername(user)
                prefs.saveRole(roleFromBackendOrJwt)


                val intent = if (isAdminSelected) {
                    Intent(this@LoginActivity, AdminDashboardActivity::class.java)
                } else {
                    Intent(this@LoginActivity, ClientProductsActivity::class.java)
                }

                startActivity(intent)
                finish()

            } catch (e: Exception) {
                showError("Error login: " + (e.message ?: "desconocido"))
            } finally {
                b.progress.visibility = View.GONE
            }
        }
    }

    private fun goByRole(role: String) {
        val intent = if (role.uppercase().contains("ADMIN")) {
            Intent(this, AdminDashboardActivity::class.java)
        } else {
            Intent(this, ClientProductsActivity::class.java)
        }
        startActivity(intent)
    }

    private fun showError(msg: String) {
        b.tvError.text = msg
        b.tvError.visibility = View.VISIBLE
    }
}
