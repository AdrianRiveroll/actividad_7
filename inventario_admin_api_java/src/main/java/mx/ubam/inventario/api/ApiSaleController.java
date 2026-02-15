package mx.ubam.inventario.api;

import mx.ubam.inventario.api.dto.SaleDtos;
import mx.ubam.inventario.service.SaleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class ApiSaleController {

    private final SaleService saleService;

    public ApiSaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public SaleDtos.CreateSaleResponse create(@RequestBody SaleDtos.CreateSaleRequest req) {
        List<SaleService.ItemRequest> items = req.items().stream()
                .map(i -> new SaleService.ItemRequest(i.productId(), i.quantity()))
                .toList();

        var sale = saleService.createSale(req.customerName(), items);
        return new SaleDtos.CreateSaleResponse(sale.getId(), "Venta registrada");
    }
}
