package com.harsh.lms.service;

import com.harsh.lms.exception.*;
import com.harsh.lms.model.Book;
import com.harsh.lms.model.BookIssued;
import com.harsh.lms.model.Member;
import com.harsh.lms.repository.BookIssuedRepository;
import com.harsh.lms.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private enum BookIssueStatus {
        ISSUED,
        RETURNED,
        INVALID
    }

    private final BookRepository bookRepo;
    private final BookIssuedRepository bookIssuedRepository;

    @Autowired
    public BookService(BookRepository bookRepo, BookIssuedRepository bookIssuedRepository) {
        this.bookRepo = bookRepo;
        this.bookIssuedRepository = bookIssuedRepository;
    }

    public Book searchBookById(int bookId){
        Optional<Book> searchedBook = bookRepo.findById(bookId);

        if (searchedBook.isPresent()){
            return searchedBook.get();
        }
        throw new InvalidBookException();
    }

    public List<Book> searchBookByAuthor(String authorName){
        List<Book> booksByAuthor = bookRepo.findByAuthor(authorName);
        
        if (!booksByAuthor.isEmpty()) {
            return booksByAuthor;
        }
        throw new BookNotFoundByAuthorException("No Book with Author Name: " + authorName + " is found");
    }

    public List<Book> searchBookByTitle(String bookTitle){
        List<Book> booksByTitle = bookRepo.findByTitle(bookTitle);
        
        if (!booksByTitle.isEmpty()){
            return booksByTitle;
        }
        throw new BookNotFoundByTitleException("No Book with Book Title: " + bookTitle + " is found");
    }

    private boolean hasReachedIssueLimit(List<BookIssued> memberIssuedBook) {
        return memberIssuedBook.size() == 3;
    }

    private boolean bookAlreadyIssued(List<BookIssued> memberIssuedBooks, int bookId) {
        for (BookIssued issuedBooks :  memberIssuedBooks) {
            if (issuedBooks.getBook().getBookId() == bookId) {
                return true;
            }
        }
        return false;
    }

    public void issueBook(int bookId, Member member){

        if (hasReachedIssueLimit(member.getIssuedBooks())) {
            throw new IssueLimitReachedException();
        }

        if (bookAlreadyIssued(member.getIssuedBooks(), bookId)) {
            throw new BookAlreadyIssuedException();
        }

        Optional<Book> bookToIssue =  bookRepo.findById(bookId);
        
        if (bookToIssue.isPresent()) {
            int bookAvailableQty = bookToIssue.get().getAvailableQty();
            
            if (bookAvailableQty <= 0) {
                throw new BookNotAvailableException();
            }
        } else {
            throw new InvalidBookException();
        }

        createBookIssueEntry(bookToIssue.get(), member);
    }
    
    private void createBookIssueEntry(Book bookToIssue, Member member) {

        LocalDate todaysDate = LocalDate.now();
        LocalDate returnDate = todaysDate.plusDays(7);

        BookIssued bookIssued = new BookIssued();
        bookIssued.setIssuedDate(todaysDate);
        bookIssued.setReturnDate(returnDate);
        bookIssued.setMember(member);
        bookIssued.setBook(bookToIssue);

        List<BookIssued> currIssuedBooks = member.getIssuedBooks();
        currIssuedBooks.add(bookIssued);
        member.setIssuedBooks(currIssuedBooks);
        bookIssuedRepository.save(bookIssued);

        updateBookStock(bookToIssue, BookIssueStatus.ISSUED);
    }

    public void returnBook(int bookId, Member member) {
        Optional<Book> bookToReturn = bookRepo.findById(bookId);

        if (member.getIssuedBooks().isEmpty()) {
            throw new BookIssuedNotFoundException();
        }

        if (!bookToReturn.isPresent()) {
            throw new InvalidBookException();
        }

        createBookReturnEntry(bookToReturn.get(), member);
    }

    public void createBookReturnEntry(Book book, Member member) {
        Optional<BookIssued> bookToReturn = bookIssuedRepository.findByBook_BookIdAndMember_MemberId(book.getBookId(), member.getMemberId());

        if (bookToReturn.isPresent()) {
            bookIssuedRepository.deleteById(bookToReturn.get().getIssueId());
            List<BookIssued> updatedBooksIssuedList = bookIssuedRepository.findAllByMember_MemberId(member.getMemberId());
            member.setIssuedBooks(updatedBooksIssuedList);
            updateBookStock(book, BookIssueStatus.RETURNED);
        }
    }

    private void updateBookStock(Book book, BookIssueStatus status) {
        if (status.equals(BookIssueStatus.ISSUED)) {
            book.setAvailableQty(book.getAvailableQty() - 1);
            bookRepo.save(book);
            return;
        } else if (status.equals(BookIssueStatus.RETURNED)) {
            book.setAvailableQty(book.getAvailableQty() + 1);
            bookRepo.save(book);
            return;
        }

        throw new InvalidBookException();
    }

    public List<Book> viewAllBooks(String role){
        List<Book> allBooksOfLibrary = bookRepo.findAll();

        if (!allBooksOfLibrary.isEmpty()) {
            return allBooksOfLibrary;
        } else {
            throw new RuntimeException("Library is empty — no books found");
        }
    }

    public void removeBook(int bookId){
        if (bookRepo.existsById(bookId)) {
            bookRepo.deleteById(bookId);
        } else {
            throw new InvalidBookException();
        }
    }

    public void addNewBook(Book book){
        if (book.getAvailableQty() <= 0) {
            throw new InvalidBookStockException("Book addition failed — invalid stock value.");
        }
        if (bookRepo.existsById(book.getBookId())) {
            throw new BookAlreadyExistsException("Book already exists with this ID!");
        }
        bookRepo.save(book);
    }
}