package mx.ubam.inventarioapp.data

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val sp: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    fun saveToken(token: String) = sp.edit().putString("token", token).apply()
    fun getToken(): String? = sp.getString("token", null)
    fun saveUsername(username: String) = sp.edit().putString("username", username).apply()
    fun getUsername(): String? = sp.getString("username", null)
    fun saveRole(role: String) = sp.edit().putString("role", role).apply()
    fun getRole(): String? = sp.getString("role", null)
    fun clear() = sp.edit().clear().apply()
}
