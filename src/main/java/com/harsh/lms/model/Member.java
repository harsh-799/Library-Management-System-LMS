package com.harsh.lms.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId;
    private String fullName;
    private final String role = "member";

    @OneToOne(mappedBy = "member")
    private MemberCredentials memberCredentials;

    @OneToMany(mappedBy = "member")
    private List<BookIssued> issuedBooks;

    public Member() {

    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public MemberCredentials getMemberCredentials() {
        return memberCredentials;
    }

    public void setMemberCredentials(MemberCredentials memberCredentials) {
        this.memberCredentials = memberCredentials;
    }

    public List<BookIssued> getIssuedBooks() {
        return issuedBooks;
    }

    public void setIssuedBooks(List<BookIssued> issuedBooks) {
        this.issuedBooks = issuedBooks;
    }

}
