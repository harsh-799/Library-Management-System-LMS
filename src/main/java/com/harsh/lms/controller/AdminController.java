package com.harsh.lms.controller;

import com.harsh.lms.dto.GetMemberResponse;
import com.harsh.lms.dto.RegisterMemberRequest;
import com.harsh.lms.dto.RegisterMemberResponse;
import com.harsh.lms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private MemberService memberService;

    @Autowired
    public AdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/member")
    public RegisterMemberResponse createNewMember(@RequestBody RegisterMemberRequest registerMemberRequest) {
        return memberService.addMember(registerMemberRequest);
    }


}
