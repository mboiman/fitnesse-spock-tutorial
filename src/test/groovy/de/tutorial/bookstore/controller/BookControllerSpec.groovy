package de.tutorial.bookstore.controller

import de.tutorial.bookstore.model.Book
import de.tutorial.bookstore.repository.BookRepository
import spock.lang.Specification
import spock.lang.Subject
import org.springframework.http.HttpStatus

class BookControllerSpec extends Specification {
    
    def bookRepository = Mock(BookRepository)
    
    @Subject
    def controller = new BookController(bookRepository)
    
    def "sollte alle Bücher zurückgeben"() {
        given: "Mehrere Bücher in der Datenbank"
        def books = [
            new Book("978-3-16-148410-0", "Clean Code", "Robert C. Martin", new BigDecimal("39.95")),
            new Book("978-0-321-35668-0", "Effective Java", "Joshua Bloch", new BigDecimal("45.50"))
        ]
        bookRepository.findAll() >> books
        
        when: "Alle Bücher abgerufen werden"
        def result = controller.getAllBooks()
        
        then: "Werden alle Bücher zurückgegeben"
        result.size() == 2
        result[0].title == "Clean Code"
        result[1].title == "Effective Java"
    }
    
    def "sollte Buch nach ID finden"() {
        given: "Ein existierendes Buch"
        def book = new Book("978-3-16-148410-0", "Clean Code", "Robert C. Martin", new BigDecimal("39.95"))
        book.id = 1L
        bookRepository.findById(1L) >> Optional.of(book)
        
        when: "Das Buch abgerufen wird"
        def response = controller.getBookById(1L)
        
        then: "Wird das Buch zurückgegeben"
        response.statusCode == HttpStatus.OK
        response.body.title == "Clean Code"
    }
    
    def "sollte 404 zurückgeben wenn Buch nicht gefunden"() {
        given: "Kein Buch mit dieser ID"
        bookRepository.findById(999L) >> Optional.empty()
        
        when: "Ein nicht existierendes Buch abgerufen wird"
        def response = controller.getBookById(999L)
        
        then: "Wird 404 NOT FOUND zurückgegeben"
        response.statusCode == HttpStatus.NOT_FOUND
    }
    
    def "sollte Bücher durchsuchen können"() {
        given: "Suchergebnisse"
        def searchResults = [
            new Book("978-3-16-148410-0", "Clean Code", "Robert C. Martin", new BigDecimal("39.95"))
        ]
        
        when: "Nach 'Clean' gesucht wird"
        def results = controller.searchBooks("Clean")
        
        then: "Repository wurde mit Kleinbuchstaben aufgerufen und gibt Ergebnisse zurück"
        1 * bookRepository.searchBooks("clean") >> searchResults
        
        and: "Werden passende Bücher gefunden"
        results.size() == 1
        results[0].title == "Clean Code"
    }
    
    def "sollte neues Buch erstellen"() {
        given: "Ein neues Buch"
        def newBook = new Book("978-1-234567-89-0", "Test Buch", "Test Autor", new BigDecimal("29.99"))
        bookRepository.save(newBook) >> { Book b -> 
            b.id = 1L
            return b
        }
        
        when: "Das Buch erstellt wird"
        def result = controller.createBook(newBook)
        
        then: "Wird das erstellte Buch zurückgegeben"
        result.id == 1L
        result.title == "Test Buch"
    }
    
    def "sollte Buch aktualisieren"() {
        given: "Ein existierendes Buch"
        def updatedBook = new Book("978-3-16-148410-0", "Clean Code Updated", "Robert C. Martin", new BigDecimal("42.99"))
        bookRepository.existsById(1L) >> true
        bookRepository.save(_) >> { Book b -> b }
        
        when: "Das Buch aktualisiert wird"
        def response = controller.updateBook(1L, updatedBook)
        
        then: "Wird das aktualisierte Buch zurückgegeben"
        response.statusCode == HttpStatus.OK
        response.body.id == 1L
        response.body.title == "Clean Code Updated"
    }
}