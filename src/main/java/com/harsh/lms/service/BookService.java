package com.harsh.lms.service;

import com.harsh.lms.dto.*;
import com.harsh.lms.exception.*;
import com.harsh.lms.model.Book;
import com.harsh.lms.model.BookIssued;
import com.harsh.lms.model.Member;
import com.harsh.lms.repository.BookIssuedRepository;
import com.harsh.lms.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public GetBookMemberResponse searchBookById(int bookId){
        Optional<Book> searchedBook = bookRepo.findById(bookId);
        GetBookMemberResponse getBookMemberResponse = new GetBookMemberResponse();

        if (searchedBook.isPresent()){
            Book book = searchedBook.get();
            getBookMemberResponse.setSuccess(true);
            getBookMemberResponse.setMessage("Book Found Successfully.");
            getBookMemberResponse.setBookId(book.getBookId());
            getBookMemberResponse.setAuthor(book.getAuthor());
            getBookMemberResponse.setTitle(book.getTitle());

            if (book.getAvailableQty() > 0) getBookMemberResponse.setStatus("Available");
            else getBookMemberResponse.setStatus("Out Of Stock!");

            return getBookMemberResponse;
        }

        // throw new InvalidBookException();
        getBookMemberResponse.setSuccess(false);
        getBookMemberResponse.setMessage("Book Not Found");
        return getBookMemberResponse;
    }

    public List<AllBooksMemberResponse> searchBookByAuthor(String authorName) {

        List<Book> booksByAuthor = bookRepo.findByAuthor(authorName);
        List<AllBooksMemberResponse> allBookMemberResponses = new ArrayList<>();

        if (!booksByAuthor.isEmpty()) {
            for (Book book : booksByAuthor) {

                AllBooksMemberResponse bookMemberResponse = new AllBooksMemberResponse();

                bookMemberResponse.setBookId(book.getBookId());
                bookMemberResponse.setTitle(book.getTitle());
                bookMemberResponse.setAuthor(book.getAuthor());

                int availQty = book.getAvailableQty();
                if (availQty > 0) bookMemberResponse.setStatus("Available");
                else bookMemberResponse.setStatus("Out Of Stock!");

                allBookMemberResponses.add(bookMemberResponse);
            }
        }

        return allBookMemberResponses;
    }


    public List<AllBooksMemberResponse> searchBookByTitle(String bookTitle){
        List<Book> booksByTitle = bookRepo.findAllByTitleContaining(bookTitle);
        List<AllBooksMemberResponse> allBookMemberResponses = new ArrayList<>();

        if (!booksByTitle.isEmpty()) {
            for (Book book : booksByTitle) {

                AllBooksMemberResponse bookMemberResponse = new AllBooksMemberResponse();

                bookMemberResponse.setBookId(book.getBookId());
                bookMemberResponse.setTitle(book.getTitle());
                bookMemberResponse.setAuthor(book.getAuthor());

                int availQty = book.getAvailableQty();
                if (availQty > 0) bookMemberResponse.setStatus("Available");
                else bookMemberResponse.setStatus("Out Of Stock!");

                allBookMemberResponses.add(bookMemberResponse);
            }
        }

        return allBookMemberResponses;

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

    public GetBookResponse viewBookById(int bookId) {
        Optional<Book> bookRecord = bookRepo.findById(bookId);
        GetBookResponse bookResponse = new GetBookResponse();

        if (bookRecord.isPresent()) {
            Book bookEntity = bookRecord.get();
            bookResponse.setStatus(true);
            bookResponse.setMessage("Book Found Successfully");
            bookResponse.setBookId(bookEntity.getBookId());
            bookResponse.setTitle(bookEntity.getTitle());
            bookResponse.setAuthor(bookEntity.getAuthor());
            bookResponse.setTotalQty(bookEntity.getTotalQty());
            bookResponse.setAvailableQty(bookEntity.getAvailableQty());

            return bookResponse;
        }

        bookResponse.setStatus(false);
        bookResponse.setMessage("Book Not Found");
        return bookResponse;
    }

    public List<AllBookResponse> viewAllBooksAdmin(){
        List<Book> allBooksOfLibrary = bookRepo.findAll();
        List<AllBookResponse> allBookResponses = new ArrayList<>();

        for (Book book : allBooksOfLibrary) {
            AllBookResponse bookResponse = new AllBookResponse();
            bookResponse.setBookId(book.getBookId());
            bookResponse.setTitle(book.getTitle());
            bookResponse.setAuthor(book.getAuthor());
            bookResponse.setTotalQty(book.getTotalQty());
            bookResponse.setAvailableQty(book.getAvailableQty());

            allBookResponses.add(bookResponse);
        }

        return allBookResponses;
    }

    public List<AllBooksMemberResponse> viewAllBooksMember() {
        List<Book> allBooksOfLibrary = bookRepo.findAll();
        List<AllBooksMemberResponse> allBookMemberResponses = new ArrayList<>();

        for (Book book : allBooksOfLibrary) {
            AllBooksMemberResponse bookMemberResponse = new AllBooksMemberResponse();
            bookMemberResponse.setBookId(book.getBookId());
            bookMemberResponse.setTitle(book.getTitle());
            bookMemberResponse.setAuthor(book.getAuthor());

            int availQty = book.getAvailableQty();
            if (availQty > 0) bookMemberResponse.setStatus("Available");
            else bookMemberResponse.setStatus("Out Of Stock!");

            allBookMemberResponses.add(bookMemberResponse);
        }

        return allBookMemberResponses;
    }

    public DeleteBookResponse removeBook(int bookId){

        Optional<Book> bookRecord = bookRepo.findById(bookId);
        DeleteBookResponse deleteBookResponse;

        if (bookRecord.isPresent()) {
            Book bookEntity = bookRecord.get();
            bookRepo.delete(bookEntity);
            deleteBookResponse = new DeleteBookResponse();
            deleteBookResponse.setStatus(true);
            deleteBookResponse.setMessage("Book Deleted Successfully");
            deleteBookResponse.setBookId(bookEntity.getBookId());
            return deleteBookResponse;
        } else {
            // throw new InvalidBookException();
            deleteBookResponse = new DeleteBookResponse();
            deleteBookResponse.setStatus(false);
            deleteBookResponse.setMessage("Book Not Found");
            return deleteBookResponse;
        }
    }

    public RegisterBookResponse addNewBook(RegisterBookRequest newBook){

        RegisterBookResponse registerBookResponse;

        if (newBook.getTotalQty() <= 0) {
            registerBookResponse = new RegisterBookResponse();
            registerBookResponse.setStatus(false);
            registerBookResponse.setMessage("Book addition failed — invalid stock value.");
            return registerBookResponse;
            // throw new InvalidBookStockException("Book addition failed — invalid stock value.");
        }
        if (bookRepo.existsById(newBook.getBookId())) {
            registerBookResponse = new RegisterBookResponse();
            registerBookResponse.setStatus(false);
            registerBookResponse.setMessage("Book already exists with this ID!");
            return registerBookResponse;
            // throw new BookAlreadyExistsException("Book already exists with this ID!");
        }

        Book book= new Book(newBook.getBookId(), newBook.getBookTitle(), newBook.getBookAuthor(), newBook.getTotalQty());
        bookRepo.save(book);
        registerBookResponse = new RegisterBookResponse();
        registerBookResponse.setStatus(true);
        registerBookResponse.setMessage("Book added Successfully");
        return registerBookResponse;
    }
}