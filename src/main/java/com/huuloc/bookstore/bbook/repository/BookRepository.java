package com.huuloc.bookstore.bbook.repository;

import com.huuloc.bookstore.bbook.entity.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Book findBySlug(String slug);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT b FROM books b WHERE b.id = ?1")
    Book findByIdWithLock(Long id);
}
