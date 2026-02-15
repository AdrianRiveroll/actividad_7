# Inventario + Ventas (Admin Web) + API (Cliente) - Java (Spring Boot)

Incluye:
- Admin web: alta/edición/baja de productos, registrar venta, ver ventas.
- Cliente por API: ver productos y stock.
- Seguridad: Admin (form login) / API (JWT).

## Requisitos
- JDK 17
- MySQL 8.x (o MariaDB)
- Maven

## 1) Crear BD
```sql
CREATE DATABASE inventario_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 2) Configurar MySQL
Edita `src/main/resources/application.properties` y pon tu password.

## 3) Ejecutar
```bash
mvn spring-boot:run
```

## 4) Accesos
Admin:
- http://localhost:8080/admin/products
- admin / admin123

API (cliente):
- POST http://localhost:8080/api/auth/login
Body: {"username":"client","password":"client123"}
Luego:
- GET http://localhost:8080/api/products
Header: Authorization: Bearer <token>
