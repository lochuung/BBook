package com.huuloc.bookstore.bbook.repository;

import com.huuloc.bookstore.bbook.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Book findBySlug(String slug);
}
