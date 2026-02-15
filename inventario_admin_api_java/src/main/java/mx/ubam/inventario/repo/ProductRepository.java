package mx.ubam.inventario.repo;

import mx.ubam.inventario.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where lower(p.name) like lower(concat('%', :q, '%')) or lower(p.brand) like lower(concat('%', :q, '%'))")
    List<Product> search(String q);
}
