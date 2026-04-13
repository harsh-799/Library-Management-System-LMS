package com.harsh.lms.controller;

import com.harsh.lms.dto.*;
import com.harsh.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/book")
    public RegisterBookResponse addBook(@RequestBody RegisterBookRequest registerBookRequest) {
        return bookService.addNewBook(registerBookRequest);
    }

    @DeleteMapping("/book/{id}")
    public DeleteBookResponse deleteBookById(@PathVariable(name = "id") int bookId) {
        return bookService.removeBook(bookId);
    }

    @GetMapping("/book/{id}")
    public GetBookResponse getBookByIdAdmin(@PathVariable(name = "id") int bookId) {
        return bookService.viewBookById(bookId);
    }

    @GetMapping("/books")
    public List<AllBookResponse> getAllBooksAdmin() {
        return bookService.viewAllBooksAdmin();
    }

    @GetMapping("/member/books")
    public List<AllBooksMemberResponse> getAllBooksMember() {
        return bookService.viewAllBooksMember();
    }

    @GetMapping(value="/book/search", params = "bookId")
    public GetBookMemberResponse getBookByIdMember(@RequestParam int bookId) {
        return bookService.searchBookById(bookId);
    }

    @GetMapping(value = "/book/search", params = "name")
    public List<AllBooksMemberResponse> getBookByAuthorName(@RequestParam String name) {
        return bookService.searchBookByAuthor(name);
    }

    @GetMapping(value = "/book/search", params = "title")
    public List<AllBooksMemberResponse> getBookByTitle(@RequestParam String title) {
        return bookService.searchBookByTitle(title);
    }

    @PostMapping("member/issue")
    public IssueBookResponse issueBook(@RequestBody IssueBookRequest bookIssueRequest) {
        return bookService.issueBook(bookIssueRequest);
    }

    @PostMapping("member/return")
    public ReturnBookResponse returnBook(@RequestBody ReturnBookRequest returnBookRequest) {
        return bookService.returnBook(returnBookRequest);
    }
}
