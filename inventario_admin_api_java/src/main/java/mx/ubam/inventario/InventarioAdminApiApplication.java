package mx.ubam.inventario;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventarioAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventarioAdminApiApplication.class, args);
    }

    @Bean
    CommandLineRunner printUrls() {
        return args -> {

            System.out.println("Pagina principal:   http://localhost:8080/");


        };
    }
}
