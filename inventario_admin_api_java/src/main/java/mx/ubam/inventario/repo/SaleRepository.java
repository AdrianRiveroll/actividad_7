package mx.ubam.inventario.repo;

import mx.ubam.inventario.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByCustomerNameOrderByCreatedAtDesc(String customerName);
}
