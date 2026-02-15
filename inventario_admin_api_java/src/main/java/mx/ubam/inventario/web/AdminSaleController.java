package mx.ubam.inventario.web;

import mx.ubam.inventario.repo.ProductRepository;
import mx.ubam.inventario.repo.SaleRepository;
import mx.ubam.inventario.service.SaleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/sales")
public class AdminSaleController {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SaleService saleService;

    public AdminSaleController(SaleRepository saleRepository, ProductRepository productRepository, SaleService saleService) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.saleService = saleService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("sales", saleRepository.findAll());
        return "admin/sales";
    }

    @GetMapping("/new")
    public String newSale(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/sale_new";
    }

    @PostMapping("/new")
    public String createSale(@RequestParam String customerName,
                             @RequestParam(name="productId") List<Long> productIds,
                             @RequestParam(name="quantity") List<Integer> quantities,
                             Model model) {
        try {
            List<SaleService.ItemRequest> items = new ArrayList<>();
            for (int i = 0; i < productIds.size(); i++) {
                Long pid = productIds.get(i);
                Integer q = quantities.get(i);
                if (pid != null && q != null && q > 0) items.add(new SaleService.ItemRequest(pid, q));
            }
            saleService.createSale(customerName, items);
            return "redirect:/admin/sales";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("products", productRepository.findAll());
            return "admin/sale_new";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("sale", saleRepository.findById(id).orElseThrow());
        return "admin/sale_detail";
    }
}
