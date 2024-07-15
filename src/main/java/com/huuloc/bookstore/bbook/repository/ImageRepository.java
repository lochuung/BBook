package com.huuloc.bookstore.bbook.repository;

import com.huuloc.bookstore.bbook.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>{
    void deleteByName(String name);
}
