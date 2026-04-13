package com.harsh.lms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMemberResponse {
    private boolean status;
    private String message;
    private Integer memberId;
    private String memberName;
    private Integer totalIssuedBooks;
    private List<Integer> issuedBooksIds;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public List<Integer> getIssuedBooksIds() {
        return issuedBooksIds;
    }

    public void setIssuedBooksIds(List<Integer> issuedBooksIds) {
        this.issuedBooksIds = issuedBooksIds;
    }
}
