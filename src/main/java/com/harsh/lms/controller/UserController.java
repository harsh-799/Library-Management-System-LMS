package com.harsh.lms.controller;

import com.harsh.lms.dto.*;
import com.harsh.lms.service.BookService;
import com.harsh.lms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private BookService bookService;
    private MemberService memberService;

    @Autowired
    public UserController(BookService bookService, MemberService memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
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

    @PostMapping("member/issue")
    public IssueBookResponse issueBook(@RequestBody IssueBookRequest bookIssueRequest) {
        return bookService.issueBook(bookIssueRequest);
    }

    @PostMapping("member/return")
    public ReturnBookResponse returnBook(@RequestBody ReturnBookRequest returnBookRequest) {
        return bookService.returnBook(returnBookRequest);
    }

    @GetMapping("/member/issues/{id}")
    public MemberIssuedBookResponse getIssuedBooks(@PathVariable(name = "id") int memberId) {
        return memberService.getAllIssued(memberId);
    }
    
    @PutMapping("/member/password")
    public ChangeMemberPasswordResponse changeMemberPassword(@RequestBody ChangeMemberPasswordRequest changeMemberPasswordRequest) {
        return memberService.updatePassword(changeMemberPasswordRequest);
    }


}
