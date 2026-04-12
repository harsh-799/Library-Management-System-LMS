package com.harsh.lms.controller;

import com.harsh.lms.dto.AllBooksMemberResponse;
import com.harsh.lms.dto.GetBookMemberResponse;
import com.harsh.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private BookService bookService;

    @Autowired
    public UserController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/member/books")
    public List<AllBooksMemberResponse> getAllBooks() {
        return bookService.viewAllBooksMember();
    }

    @GetMapping(value="/book/search", params = "bookId")
    public GetBookMemberResponse getBookById(@RequestParam int bookId) {
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


}
