package edu.syr.Used_Book.service;

import edu.syr.Used_Book.exception.BookNotFoundException;
import edu.syr.Used_Book.exception.InvalidBookOperationException;
import edu.syr.Used_Book.model.Book;
import edu.syr.Used_Book.pricing.BasicBookPrice;
import edu.syr.Used_Book.pricing.BookPrice;
import edu.syr.Used_Book.pricing.DepreciatedBookPrice;
import edu.syr.Used_Book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service  // Marks this class as a Spring-managed service component
public class BookService {

    @Autowired  // Injects the BookRepository (handles DB operations)
    private BookRepository bookRepository;

    /**
     * Get all books currently available in inventory.
     * @return List of all books
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findAll();  // Fetch all rows from the book table
    }

    /**
     * Simulate buying a book. The book is removed from the system by its unique ID.
     * @param id The unique ID of the book
     * @return true if purchase was successful, false if book not found
     */
    public boolean buyBook(String id) {
        Optional<Book> book = bookRepository.findById(id);  // Look up book by ID

        if (book.isEmpty()) {
            // If not found, throw 404 error
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        // If found, delete it from inventory (simulate purchase)
        bookRepository.deleteById(id);
        return true;
    }

    /**
     * Sell a previously bought book. A new copy is added with 10% depreciation in price.
     * @param id ID of the original book being resold
     * @return new price after depreciation
     */
    public double sellBook(String id) {
        Optional<Book> optional = bookRepository.findById(id);  // Find book by ID

        if (optional.isEmpty()) {
            throw new BookNotFoundException("Book with ID " + id + " not found for resale.");
        }

        Book oldBook = optional.get();  // Retrieve book

        // Use Decorator Pattern to apply 10% depreciation
        BookPrice base = new BasicBookPrice(oldBook.getPrice());
        BookPrice depreciated = new DepreciatedBookPrice(base);  // Wrap with depreciation logic
        double newPrice = depreciated.getPrice();  // Get updated price

        // Create a new book copy with depreciated price and new UUID
        Book newBook = new Book(
                UUID.randomUUID().toString(),
                oldBook.getIsbn(),
                oldBook.getTitle(),
                oldBook.getAuthor(),
                oldBook.getEdition(),
                newPrice
        );

        // Add back to inventory
        bookRepository.save(newBook);
        return newPrice;
    }

    /**
     * Sell a book using only its ISBN. Finds a matching copy and applies depreciation.
     */
    public double sellBookByIsbn(String isbn) {
        Optional<Book> optional = bookRepository.findByIsbn(isbn);  // Search by ISBN

        if (optional.isEmpty()) {
            throw new BookNotFoundException("No book with ISBN " + isbn + " found.");
        }

        Book oldBook = optional.get();

        // Apply 10% depreciation using decorator
        BookPrice base = new BasicBookPrice(oldBook.getPrice());
        BookPrice depreciated = new DepreciatedBookPrice(base);
        double newPrice = depreciated.getPrice();

        // Create and save a new copy of the book
        Book newBook = new Book(
                UUID.randomUUID().toString(),
                oldBook.getIsbn(),
                oldBook.getTitle(),
                oldBook.getAuthor(),
                oldBook.getEdition(),
                newPrice
        );

        bookRepository.save(newBook);
        return newPrice;
    }

    /**
     * Sell a completely new book (new to the system).
     * Allows multiple copies with the same ISBN by giving each a unique ID.
     *  isbn Book ISBN
     *  title Book title
     *  author Author name
     *  edition Edition info
     *   price Original price
     * return price as confirmation
     */
    public double sellNewBook(String isbn, String title, String author, String edition, double price) {
        // Validate input: no null/blank values, price must be positive
        if (isbn == null || isbn.isBlank() || title == null || title.isBlank() || price <= 0) {
            throw new InvalidBookOperationException("Invalid book data provided.");
        }

        // Even if a book with same ISBN exists, allow creating a new one (duplicate copies allowed)
        Book book = new Book(
                UUID.randomUUID().toString(),  // New unique ID
                isbn,
                title,
                author,
                edition,
                price
        );

        // Save to inventory
        bookRepository.save(book);
        return price;
    }
}
