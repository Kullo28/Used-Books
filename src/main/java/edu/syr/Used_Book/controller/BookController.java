
package edu.syr.Used_Book.controller;

import edu.syr.Used_Book.model.Book;
import edu.syr.Used_Book.service.BookService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellNewBook(@Valid @RequestBody Book book) {
        double price = bookService.sellNewBook(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getEdition(), book.getPrice());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "success", "price", price));
    }

    @PostMapping("/buy/{id}")
    public ResponseEntity<?> buyBook(@PathVariable String id) {
        bookService.buyBook(id);
        return ResponseEntity.ok(Map.of("status", "success", "id", id));
    }

    @PostMapping("/sell/{id}")
    public ResponseEntity<?> sellBook(@PathVariable String id) {
        double price = bookService.sellBook(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "success", "price", price));
    }

    @PostMapping("/sell/isbn/{isbn}")
    public ResponseEntity<?> sellBookByIsbn(@PathVariable String isbn) {
        double price = bookService.sellBookByIsbn(isbn);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "success", "price", price));
    }
}
