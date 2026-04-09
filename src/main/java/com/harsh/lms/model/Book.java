package com.harsh.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Book {

    @Id
    private int bookId;
    private String title;
    private String author;
    private int totalQty;
    private int availableQty;

    @OneToMany(mappedBy = "book")
    private List<BookIssued> issuedBooks;

    public Book() {

    }

    public Book(int bookId, String title, String author, int totalQty){
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.totalQty = totalQty;
        this.availableQty = totalQty;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public List<BookIssued> getIssuedBooks() {
        return issuedBooks;
    }

    public void setIssuedBooks(List<BookIssued> issuedBooks) {
        this.issuedBooks = issuedBooks;
    }

}