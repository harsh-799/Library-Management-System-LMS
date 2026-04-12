package com.harsh.lms.dto;

import com.harsh.lms.model.BookIssued;
import java.util.List;

public class AllMemberResponse {
    private Integer memberId;
    private String memberName;
    private Integer totalIssuedBooks;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getTotalIssuedBooks() {
        return totalIssuedBooks;
    }

    public void setTotalIssuedBooks(Integer totalIssuedBooks) {
        this.totalIssuedBooks = totalIssuedBooks;
    }
}
