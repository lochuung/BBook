package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.dto.TopBooksDto;
import com.huuloc.bookstore.bbook.dto.filter.SearchRequest;
import com.huuloc.bookstore.bbook.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> search(SearchRequest searchRequest);

    TopBooksDto getTopBooks(Integer limit);
}
