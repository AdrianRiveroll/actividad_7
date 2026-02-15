package mx.ubam.inventario.api.dto;

import java.util.List;

public class SaleDtos {
    public record CreateSaleRequest(String customerName, List<Item> items) {
        public record Item(Long productId, int quantity) {}
    }
    public record CreateSaleResponse(Long saleId, String message) {}
}
