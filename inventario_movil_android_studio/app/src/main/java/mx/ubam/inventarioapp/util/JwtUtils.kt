package mx.ubam.inventarioapp.util

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    fun roleFromToken(token: String): String {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return "CLIENT"
            val payload = decodeBase64Url(parts[1])
            val json = JSONObject(payload).toString()
            when {
                json.contains("ROLE_ADMIN") -> "ADMIN"
                json.contains("ROLE_CLIENT") -> "CLIENT"
                else -> "CLIENT"
            }
        } catch (e: Exception) {
            "CLIENT"
        }
    }

    private fun decodeBase64Url(s: String): String {
        var t = s.replace('-', '+').replace('_', '/')
        val pad = t.length % 4
        if (pad != 0) t += "=".repeat(4 - pad)
        return String(Base64.decode(t, Base64.DEFAULT))
    }
}
