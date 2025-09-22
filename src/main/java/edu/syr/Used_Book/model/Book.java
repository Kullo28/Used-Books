package edu.syr.Used_Book.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Book {

    @Id
    private String id;

    @NotBlank(message = "ISBN must not be blank")
    private String isbn;

    @NotBlank(message = "Title must not be blank")
    private String title;
    private String author;
    private String edition;

    @Positive(message = "Price must be positive")
    private double price;


    // No-arg constructor required by JPA
    public Book() {
    }

    public Book(String id, String isbn, String title, String author, String edition, double price) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.price = price;
    }

    // Getters and Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
