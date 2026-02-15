package mx.ubam.inventario.service;

import mx.ubam.inventario.domain.Product;
import mx.ubam.inventario.domain.Sale;
import mx.ubam.inventario.domain.SaleItem;
import mx.ubam.inventario.repo.ProductRepository;
import mx.ubam.inventario.repo.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SaleService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    public SaleService(ProductRepository productRepository, SaleRepository saleRepository) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
    }

    @Transactional
    public Sale createSale(String customerName, List<ItemRequest> items) {
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("La venta no tiene productos");

        Sale sale = new Sale();
        sale.setCustomerName(customerName == null || customerName.isBlank() ? "Cliente" : customerName);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemRequest req : items) {
            Product p = productRepository.findById(req.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no existe: " + req.productId()));
            if (req.quantity() <= 0) throw new IllegalArgumentException("Cantidad inválida para producto " + p.getId());
            if (p.getStock() < req.quantity()) throw new IllegalArgumentException("Stock insuficiente para " + p.getName() + " (stock=" + p.getStock() + ")");

            p.setStock(p.getStock() - req.quantity());
            productRepository.save(p);

            SaleItem si = new SaleItem();
            si.setSale(sale);
            si.setProduct(p);
            si.setQuantity(req.quantity());
            si.setUnitPrice(p.getPrice());
            si.setLineTotal(p.getPrice().multiply(BigDecimal.valueOf(req.quantity())));

            sale.getItems().add(si);
            total = total.add(si.getLineTotal());
        }

        sale.setTotal(total);
        return saleRepository.save(sale);
    }

    public record ItemRequest(Long productId, int quantity) {}
}
