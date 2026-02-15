ANDROID (InventarioMovil) - Notas rápidas
----------------------------------------
1) Emulador usa: http://10.0.2.2:8080  (NO localhost)
2) Login: POST /api/auth/login  -> debe devolver {"token":"..."} y opcional {"role":"ADMIN|CLIENT"}.
   Si no manda role, la app intenta detectar ROLE_ADMIN/ROLE_CLIENT leyendo el JWT.
3) Para que Admin pueda crear/editar/eliminar productos y ver ventas, tu backend debe tener:
   - POST/PUT/DELETE /api/products
   - GET /api/sales
   - GET /api/sales/{id}
   - POST /api/sales  (admin y cliente)
Si no existen, la app seguirá funcionando para cliente si /api/products y /api/sales existen.
