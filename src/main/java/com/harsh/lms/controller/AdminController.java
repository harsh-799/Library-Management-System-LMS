package com.harsh.lms.controller;

import com.harsh.lms.dto.*;
import com.harsh.lms.service.BookService;
import com.harsh.lms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    private MemberService memberService;
    private final BookService bookService;

    @Autowired
    public AdminController(MemberService memberService, BookService bookService) {
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @PostMapping("/member")
    public RegisterMemberResponse createNewMember(@RequestBody RegisterMemberRequest registerMemberRequest) {
        return memberService.addMember(registerMemberRequest);
    }

    @DeleteMapping("/member/{id}")
    public DeleteMemberResponse deleteMemberById(@PathVariable(name = "id") int memberId) {
        return memberService.deleteMember(memberId);
    }

    @GetMapping("/members")
    public List<AllMemberResponse> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/member/{id}")
    public GetMemberResponse getMemberDetailsById(@PathVariable(name = "id") int memberId) {
        return memberService.getMemberByID(memberId);
    }

    @PostMapping("/book")
    public RegisterBookResponse addBook(@RequestBody RegisterBookRequest registerBookRequest) {
        return bookService.addNewBook(registerBookRequest);
    }

    @DeleteMapping("/book/{id}")
    public DeleteBookResponse deleteBookById(@PathVariable(name = "id") int bookId) {
        return bookService.removeBook(bookId);
    }
}
