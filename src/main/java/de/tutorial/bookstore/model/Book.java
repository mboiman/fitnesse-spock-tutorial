package de.tutorial.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "ISBN ist erforderlich")
    @Pattern(regexp = "^(97[89])-\\d{1,5}-\\d{1,7}-\\d{1,6}-\\d$", 
             message = "ISBN muss dem Format 978-X-XXXXX-XXX-X entsprechen")
    @Column(unique = true)
    private String isbn;
    
    @NotBlank(message = "Titel ist erforderlich")
    @Size(min = 1, max = 200)
    private String title;
    
    @NotBlank(message = "Autor ist erforderlich")
    private String author;
    
    @NotNull(message = "Preis ist erforderlich")
    @DecimalMin(value = "0.01", message = "Preis muss mindestens 0.01 sein")
    @DecimalMax(value = "9999.99", message = "Preis darf maximal 9999.99 sein")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    @Min(value = 0, message = "Lagerbestand kann nicht negativ sein")
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;
    
    private String category;
    
    @Column(length = 2000)
    private String description;
    
    // Konstruktoren
    public Book() {}
    
    public Book(String isbn, String title, String author, BigDecimal price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
    }
    
    // Geschäftsmethoden
    public boolean isAvailable() {
        return stockQuantity != null && stockQuantity > 0;
    }
    
    public void decreaseStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new IllegalArgumentException("Nicht genügend Bücher auf Lager");
        }
        stockQuantity -= quantity;
    }
    
    public void increaseStock(int quantity) {
        stockQuantity += quantity;
    }
    
    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}