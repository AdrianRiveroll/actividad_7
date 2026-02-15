package mx.ubam.inventarioapp.data.model

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String?, val role: String? = null)

data class Product(
    val id: Long,
    val name: String,
    val brand: String,
    val storageCapacity: String?,
    val stock: Int,
    val price: Double
)

data class SaleItemRequest(val productId: Long, val quantity: Int)
data class CreateSaleRequest(val customerName: String, val items: List<SaleItemRequest>)

data class Sale(
    val id: Long,
    val customerName: String?,
    val createdAt: String?,
    val total: Double?
)

data class SaleItem(
    val quantity: Int,
    val unitPrice: Double?,
    val lineTotal: Double?,
    val product: Product?
)

data class SaleDetail(
    val id: Long,
    val customerName: String?,
    val createdAt: String?,
    val total: Double?,
    val items: List<SaleItem>?
)
