package com.harsh.lms.controller;

import com.harsh.lms.dto.AllBooksMemberResponse;
import com.harsh.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
