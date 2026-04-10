package com.harsh.lms.repository;

import com.harsh.lms.model.BookIssued;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookIssuedRepository extends JpaRepository<BookIssued, Integer> {

    List<BookIssued> findAllByMember_MemberId(int memberId);

    Optional<BookIssued> findByBook_BookIdAndMember_MemberId(int bookId, int memberId);
}
