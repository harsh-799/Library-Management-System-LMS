package com.harsh.lms.service;

import com.harsh.lms.dto.GetMemberResponse;
import com.harsh.lms.dto.RegisterMemberRequest;
import com.harsh.lms.dto.RegisterMemberResponse;
import com.harsh.lms.exception.BookIssuedNotFoundException;
import com.harsh.lms.exception.IncorrectPasswordException;
import com.harsh.lms.exception.LibraryHasNoMembersException;
import com.harsh.lms.exception.MemberNotFoundException;
import com.harsh.lms.model.BookIssued;
import com.harsh.lms.model.Member;
import com.harsh.lms.model.MemberCredentials;
import com.harsh.lms.repository.BookIssuedRepository;
import com.harsh.lms.repository.MemberCredentialsRepository;
import com.harsh.lms.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepo;
    private final MemberCredentialsRepository memberCredentialsRepository;
    private final BookIssuedRepository bookIssuedRepository;

    @Autowired
    public MemberService(MemberRepository memberRepo, MemberCredentialsRepository memberCredentialsRepository, BookIssuedRepository bookIssuedRepository) {
        this.memberRepo = memberRepo;
        this.memberCredentialsRepository = memberCredentialsRepository;
        this.bookIssuedRepository = bookIssuedRepository;
    }

    public void removeMember(int memberId){
        if (memberRepo.existsById(memberId)) {
            memberRepo.deleteById(memberId);
        } else {
            throw new MemberNotFoundException();
        }
    }

    @Transactional
    public List<Member> getAllMembers(){
        List<Member> libraryMembers = memberRepo.findAll();

        if (libraryMembers.isEmpty()){
            throw new LibraryHasNoMembersException();
        }

        // Well, It's just a workaround to fix lazyinitException, Will move to DTO soon.
        for (Member mem : libraryMembers) {
            mem.getIssuedBooks().size();
            mem.getMemberCredentials().getUsername();
        }

        return libraryMembers;
    }

    public Member getMemberByID(int memberID){
        Optional<Member> result = memberRepo.findById(memberID);
        if (result.isEmpty()){
            throw new RuntimeException("No member Found with that ID.");
        }

        return result.get();
    }

    public Member syncMemberOnLogin(Member member){
        List<BookIssued> bookIssuedToMember = bookIssuedRepository.findAllByMember_MemberId(member.getMemberId());

        member.setIssuedBooks(bookIssuedToMember);
        return member;
    }

    public List<BookIssued> getAllIssued(Member member) {
        if (member.getIssuedBooks().isEmpty()) {
            throw new BookIssuedNotFoundException();
        }

        return member.getIssuedBooks();
    }

    public RegisterMemberResponse addMember(RegisterMemberRequest newMember) {
        Member memberToBeSaved = new Member();
        MemberCredentials memberToBeSavedCredentials = new MemberCredentials();

        memberToBeSaved.setFullName(newMember.getFullName());

        memberToBeSavedCredentials.setUsername(newMember.getUsername());
        memberToBeSavedCredentials.setPassword(newMember.getPassword());

        memberToBeSaved.setMemberCredentials(memberToBeSavedCredentials);
        memberToBeSavedCredentials.setMember(memberToBeSaved);

        try {
            memberRepo.save(memberToBeSaved);
            RegisterMemberResponse registerMemberResponse = new RegisterMemberResponse();
            registerMemberResponse.setSuccess(true);
            registerMemberResponse.setMessage("Member registred successfully.");
            return registerMemberResponse;

        } catch (DataIntegrityViolationException e) {
            RegisterMemberResponse registerMemberResponse = new RegisterMemberResponse();
            registerMemberResponse.setSuccess(false);
            registerMemberResponse.setMessage("Username is already taken! Try with something else");
            return registerMemberResponse;
        } catch (Exception e) {
            e.printStackTrace();
            RegisterMemberResponse registerMemberResponse = new RegisterMemberResponse();
            registerMemberResponse.setSuccess(false);
            registerMemberResponse.setMessage("Error! In Saving Data");
            return registerMemberResponse;
        }
    }

    public void updatePassword(Member currentMember, String oldPassword, String newPassword){
        MemberCredentials memberCredentials = currentMember.getMemberCredentials();

        if (memberCredentials.getPassword().equals(oldPassword)) {
            memberCredentials.setPassword(newPassword);
            memberCredentialsRepository.save(memberCredentials);
            return;
        }
            throw new IncorrectPasswordException();
    }
}