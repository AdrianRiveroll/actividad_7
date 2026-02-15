package mx.ubam.inventario.web;

import jakarta.validation.Valid;
import mx.ubam.inventario.domain.Product;
import mx.ubam.inventario.repo.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductRepository productRepository;

    public AdminProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        var products = (q == null || q.isBlank())
                ? productRepository.findAll()
                : productRepository.search(q);

        model.addAttribute("products", products);
        model.addAttribute("q", q == null ? "" : q);

        // IMPORTANTE: el form para el th:object
        if (!model.containsAttribute("productForm")) {
            model.addAttribute("productForm", new ProductForm());
        }

        return "admin/products";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("productForm") ProductForm form,
                         BindingResult br,
                         @RequestParam(value = "q", required = false) String q,
                         Model model) {

        if (br.hasErrors()) {
            // recargar listado para que no truene la tabla
            var products = (q == null || q.isBlank())
                    ? productRepository.findAll()
                    : productRepository.search(q);

            model.addAttribute("products", products);
            model.addAttribute("q", q == null ? "" : q);
            return "admin/products";
        }

        Product p = new Product();
        p.setName(form.getName());
        p.setBrand(form.getBrand());
        p.setStorageCapacity(form.getStorageCapacity());
        p.setStock(form.getStock());
        p.setPrice(form.getPrice());

        productRepository.save(p);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("p", productRepository.findById(id).orElseThrow());
        return "admin/product_edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String brand,
                         @RequestParam String storageCapacity,
                         @RequestParam int stock,
                         @RequestParam BigDecimal price) {

        var p = productRepository.findById(id).orElseThrow();
        p.setName(name);
        p.setBrand(brand);
        p.setStorageCapacity(storageCapacity);
        p.setStock(stock);
        p.setPrice(price);

        productRepository.save(p);
        return "redirect:/admin/products";
    }

    // ✅ FORM CLASE NORMAL (NO record)
    public static class ProductForm {

        @jakarta.validation.constraints.NotBlank(message = "El nombre no debe estar vacío")
        private String name;

        @jakarta.validation.constraints.NotBlank(message = "La marca no debe estar vacía")
        private String brand;

        @jakarta.validation.constraints.NotBlank(message = "La capacidad no debe estar vacía")
        private String storageCapacity = "N/A";

        @jakarta.validation.constraints.Min(value = 0, message = "Stock debe ser 0 o mayor")
        private int stock = 0;

        @jakarta.validation.constraints.DecimalMin(value = "0.00", message = "Precio debe ser 0 o mayor")
        private BigDecimal price = new BigDecimal("0.00");

        public ProductForm() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }

        public String getStorageCapacity() { return storageCapacity; }
        public void setStorageCapacity(String storageCapacity) { this.storageCapacity = storageCapacity; }

        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}
