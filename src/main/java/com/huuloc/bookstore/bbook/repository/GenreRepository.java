package com.huuloc.bookstore.bbook.repository;

import com.huuloc.bookstore.bbook.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
    Genre findBySlug(String slug);
}
