package mx.ubam.inventarioapp.data.net

import mx.ubam.inventarioapp.data.Prefs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080"

    fun create(prefs: Prefs): ApiService {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val req = chain.request()
                val token = prefs.getToken()
                val newReq = if (!token.isNullOrBlank()) {
                    req.newBuilder().addHeader("Authorization", "Bearer $token").build()
                } else req
                chain.proceed(newReq)
            }
            .addInterceptor(logger)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
