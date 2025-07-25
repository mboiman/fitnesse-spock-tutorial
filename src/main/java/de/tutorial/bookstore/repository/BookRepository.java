package de.tutorial.bookstore.repository;

import de.tutorial.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByIsbn(String isbn);
    
    List<Book> findByCategory(String category);
    
    List<Book> findByAuthor(String author);
    
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE %:query% OR " +
           "LOWER(b.author) LIKE %:query% OR " +
           "LOWER(b.isbn) LIKE %:query%")
    List<Book> searchBooks(@Param("query") String query);
    
    List<Book> findByStockQuantityGreaterThan(Integer quantity);
    
    @Query("SELECT DISTINCT b.category FROM Book b WHERE b.category IS NOT NULL")
    List<String> findAllCategories();
}