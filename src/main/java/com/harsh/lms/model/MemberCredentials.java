package com.harsh.lms.model;

import jakarta.persistence.*;

@Entity
public class MemberCredentials {

    @Id
    private String username;
    private String password;

    @OneToOne()
    @JoinColumn(name = "member_id")
    private Member member;

    public MemberCredentials() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
