package mx.ubam.inventario.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String brand;

    @NotBlank
    @Column(nullable = false)
    private String storageCapacity;

    @Min(0)
    @Column(nullable = false)
    private int stock;

    @DecimalMin(value = "0.00")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    public Product() {}

    public Product(String name, String brand, String storageCapacity, int stock, BigDecimal price) {
        this.name = name;
        this.brand = brand;
        this.storageCapacity = storageCapacity;
        this.stock = stock;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
