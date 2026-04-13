package com.harsh.lms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberIssuedBookResponse {
    private Boolean success;
    private String message;
    private Integer totalIssuedBooks;
    private List<Integer> issuedBooksIds;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
