package mx.ubam.inventario.api;

import mx.ubam.inventario.domain.Product;
import mx.ubam.inventario.repo.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ApiProductController {

    private final ProductRepository productRepository;

    public ApiProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> list(@RequestParam(value="q", required=false) String q) {
        if (q == null || q.isBlank()) return productRepository.findAll();
        return productRepository.search(q);
    }

    @GetMapping("/<built-in function id>")
    public Product one(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow();
    }
}
