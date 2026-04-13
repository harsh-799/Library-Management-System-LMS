package com.harsh.lms.service;

import com.harsh.lms.dto.*;
import com.harsh.lms.exception.BookIssuedNotFoundException;
import com.harsh.lms.exception.IncorrectPasswordException;
import com.harsh.lms.model.BookIssued;
import com.harsh.lms.model.Member;
import com.harsh.lms.model.MemberCredentials;
import com.harsh.lms.repository.BookIssuedRepository;
import com.harsh.lms.repository.MemberCredentialsRepository;
import com.harsh.lms.repository.MemberRepository;
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

    public DeleteMemberResponse deleteMember(int memberId){

        Optional<Member> memberRecord = memberRepo.findById(memberId);
        DeleteMemberResponse deleteMemberResponse;

        if (memberRecord.isPresent()) {
            Member memberEntity = memberRecord.get();
            memberRepo.delete(memberEntity);
            deleteMemberResponse = new DeleteMemberResponse();
            deleteMemberResponse.setSuccess(true);
            deleteMemberResponse.setMessage("Member Deleted Successfully");
            deleteMemberResponse.setMemberId(memberEntity.getMemberId());
            deleteMemberResponse.setName(memberEntity.getFullName());
            return deleteMemberResponse;
        } else {
            // throw new MemberNotFoundException();
            deleteMemberResponse = new DeleteMemberResponse();
            deleteMemberResponse.setSuccess(false);
            deleteMemberResponse.setMessage("No Member Found to delete");
            return deleteMemberResponse;
        }
    }

    public List<AllMemberResponse> getAllMembers(){
        List<Member> libraryMembers = memberRepo.findAll();
        List<AllMemberResponse> allMemberResponses = new ArrayList<>();

        if (libraryMembers.isEmpty()){
            // throw new LibraryHasNoMembersException();
            return allMemberResponses;
        }

        for (Member member : libraryMembers) {
            AllMemberResponse memberResponse  = new AllMemberResponse();
            memberResponse.setMemberId(member.getMemberId());
            memberResponse.setMemberName(member.getFullName());
            memberResponse.setTotalIssuedBooks(member.getIssuedBooks().size());

            allMemberResponses.add(memberResponse);
        }

        return allMemberResponses;
    }

    public GetMemberResponse getMemberByID(int memberID){
        Optional<Member> result = memberRepo.findById(memberID);

        if (result.isEmpty()){
            // throw new RuntimeException("No member Found with that ID.");
            GetMemberResponse getMemberResponse = new GetMemberResponse();
            getMemberResponse.setStatus(false);
            getMemberResponse.setMessage("No Member Found with that Id");
            return getMemberResponse;
        }

        Member member = result.get();

        GetMemberResponse getMemberResponse = new GetMemberResponse();

        getMemberResponse.setStatus(true);
        getMemberResponse.setMessage("Member Found Successfully");
        getMemberResponse.setMemberId(member.getMemberId());
        getMemberResponse.setUsername(member.getMemberCredentials().getUsername());
        getMemberResponse.setMemberName(member.getFullName());

        List<BookIssued> memberIssuedBooks = member.getIssuedBooks();
        getMemberResponse.setTotalIssuedBooks(memberIssuedBooks.size());

        List<Integer> bookIssued = new ArrayList<>();
        memberIssuedBooks.forEach(b -> bookIssued.add(b.getBook().getBookId()));

        getMemberResponse.setIssuedBooksIds(bookIssued);
        return getMemberResponse;
    }

    public Member syncMemberOnLogin(Member member){
        List<BookIssued> bookIssuedToMember = bookIssuedRepository.findAllByMember_MemberId(member.getMemberId());

        member.setIssuedBooks(bookIssuedToMember);
        return member;
    }

    public MemberIssuedBookResponse getAllIssued(int memberId) {

        MemberIssuedBookResponse memberIssuedBookResponse;

        Optional<Member> memberRecords = memberRepo.findById(memberId);

        if (memberRecords.isEmpty()) {
            memberIssuedBookResponse = new MemberIssuedBookResponse();
            memberIssuedBookResponse.setSuccess(false);
            memberIssuedBookResponse.setMessage("Incorrect Id. No Member Found");
            return memberIssuedBookResponse;
        }

        Member member = memberRecords.get();

        if (member.getIssuedBooks().isEmpty()) {
            memberIssuedBookResponse = new MemberIssuedBookResponse();
            memberIssuedBookResponse.setSuccess(false);
            memberIssuedBookResponse.setMessage("No Books Issued to this Member");
            return memberIssuedBookResponse;
        }

        memberIssuedBookResponse = new MemberIssuedBookResponse();
        List<BookIssued> bookIssuedList = member.getIssuedBooks();

        memberIssuedBookResponse.setSuccess(true);
        memberIssuedBookResponse.setMessage("Book details retrived Successfully.");
        memberIssuedBookResponse.setTotalIssuedBooks(bookIssuedList.size());

        List<Integer> bookIssuedIds = new ArrayList<>();
        bookIssuedList.forEach(b -> bookIssuedIds.add(b.getBook().getBookId()));

        memberIssuedBookResponse.setIssuedBooksIds(bookIssuedIds);

        return memberIssuedBookResponse;
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