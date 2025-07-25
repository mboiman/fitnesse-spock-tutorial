-- Beispiel-Testdaten für die H2 Datenbank

INSERT INTO books (isbn, title, author, price, stock_quantity, category, description) VALUES
('978-3-16-148410-0', 'Clean Code', 'Robert C. Martin', 39.95, 15, 'Software Engineering', 
 'A Handbook of Agile Software Craftsmanship - Ein Muss für jeden Entwickler'),
 
('978-0-321-35668-0', 'Effective Java', 'Joshua Bloch', 45.50, 10, 'Java', 
 'Best Practices für die Java-Plattform'),
 
('978-1-449-37320-3', 'Java 8 in Action', 'Raoul-Gabriel Urma', 42.00, 8, 'Java',
 'Lambdas, Streams, and functional-style programming'),
 
('978-0-13-468599-1', 'Refactoring', 'Martin Fowler', 47.95, 5, 'Software Engineering',
 'Improving the Design of Existing Code'),
 
('978-3-8362-7519-4', 'Java ist auch eine Insel', 'Christian Ullenboom', 49.90, 20, 'Java',
 'Das umfassende Handbuch für Java-Entwickler'),
 
('978-3-446-46408-1', 'Kotlin', 'Jörg Staudemeyer', 39.99, 12, 'Kotlin',
 'Einstieg und Praxis'),
 
('978-1-491-95020-3', 'Spring Boot in Action', 'Craig Walls', 44.95, 7, 'Spring',
 'Der praktische Einstieg in Spring Boot'),
 
('978-0-321-12742-6', 'Domain-Driven Design', 'Eric Evans', 54.95, 3, 'Software Engineering',
 'Tackling Complexity in the Heart of Software');