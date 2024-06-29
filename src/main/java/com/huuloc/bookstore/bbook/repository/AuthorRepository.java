package com.huuloc.bookstore.bbook.repository;


import com.huuloc.bookstore.bbook.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByName(String name);
}
