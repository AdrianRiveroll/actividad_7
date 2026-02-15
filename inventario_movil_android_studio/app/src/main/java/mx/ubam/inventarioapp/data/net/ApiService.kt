package mx.ubam.inventarioapp.data.net

import mx.ubam.inventarioapp.data.model.*
import retrofit2.http.*

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body req: LoginRequest): LoginResponse

    @GET("/api/products")
    suspend fun getProducts(): List<Product>

    @POST("/api/products")
    suspend fun createProduct(@Body product: Map<String, Any?>): Product

    @PUT("/api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Long, @Body product: Map<String, Any?>): Product

    @DELETE("/api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long)

    @POST("/api/sales")
    suspend fun createSale(@Body req: CreateSaleRequest): SaleDetail

    @GET("/api/sales")
    suspend fun getSales(): List<Sale>

    @GET("/api/sales/{id}")
    suspend fun getSaleDetail(@Path("id") id: Long): SaleDetail
}
