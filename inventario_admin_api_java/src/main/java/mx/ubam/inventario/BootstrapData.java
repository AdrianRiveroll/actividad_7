package mx.ubam.inventario;

import mx.ubam.inventario.domain.AppUser;
import mx.ubam.inventario.domain.Product;
import mx.ubam.inventario.repo.ProductRepository;
import mx.ubam.inventario.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder encoder;

    public BootstrapData(UserRepository userRepository, ProductRepository productRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new AppUser("admin", encoder.encode("admin123"), AppUser.Role.ADMIN));
        }
        if (!userRepository.existsByUsername("client")) {
            userRepository.save(new AppUser("cliente", encoder.encode("cliente123"), AppUser.Role.CLIENT));
        }

        if (productRepository.count() == 0) {
            productRepository.save(new Product("Teclado mecánico", "Redragon", "N/A", 25, new BigDecimal("699.00")));
            productRepository.save(new Product("Mouse gamer", "Logitech", "N/A", 30, new BigDecimal("499.00")));
            productRepository.save(new Product("USB 3.0", "Kingston", "64GB", 40, new BigDecimal("169.00")));
            productRepository.save(new Product("SSD", "Crucial", "500GB", 12, new BigDecimal("999.00")));
            productRepository.save(new Product("Audífonos", "HyperX", "N/A", 18, new BigDecimal("899.00")));
        }
    }
}
