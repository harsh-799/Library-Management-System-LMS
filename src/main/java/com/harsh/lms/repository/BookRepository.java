package com.harsh.lms.repository;

import com.harsh.lms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByAuthor(String authorName);
    List<Book> findAllByTitleContaining(String bookTitle);
}
