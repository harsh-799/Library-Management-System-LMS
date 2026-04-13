package com.harsh.lms.controller;

import com.harsh.lms.dto.*;
import com.harsh.lms.service.BookService;
import com.harsh.lms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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

    @GetMapping("/member/issues/{id}")
    public MemberIssuedBookResponse getIssuedBooks(@PathVariable(name = "id") int memberId) {
        return memberService.getAllIssued(memberId);
    }
    
    @PutMapping("/member/password")
    public ChangeMemberPasswordResponse changeMemberPassword(@RequestBody ChangeMemberPasswordRequest changeMemberPasswordRequest) {
        return memberService.updatePassword(changeMemberPasswordRequest);
    }
}
