package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.dto.TopBooksDto;
import com.huuloc.bookstore.bbook.dto.filter.SearchRequest;
import com.huuloc.bookstore.bbook.entity.Book;
import org.springframework.data.domain.Page;

public interface BookService {

    Page<Book> search(SearchRequest searchRequest);

    TopBooksDto getTopBooks(Integer limit);

    Book findBySlug(String slug);
}
