package com.huuloc.bookstore.bbook.service.impl;

import com.huuloc.bookstore.bbook.dto.TopBooksDto;
import com.huuloc.bookstore.bbook.dto.filter.SearchRequest;
import com.huuloc.bookstore.bbook.dto.filter.SortRequest;
import com.huuloc.bookstore.bbook.entity.Book;
import com.huuloc.bookstore.bbook.enums.sort.filter.SortDirection;
import com.huuloc.bookstore.bbook.exception.BadRequestException;
import com.huuloc.bookstore.bbook.repository.BookRepository;
import com.huuloc.bookstore.bbook.repository.specification.SearchSpecification;
import com.huuloc.bookstore.bbook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Page<Book> search(SearchRequest searchRequest) {
        SearchSpecification<Book> searchSpecification = new SearchSpecification<>(searchRequest);
        Pageable pageable = SearchSpecification.getPageable(searchRequest.getPage(),
                searchRequest.getSize());
        return bookRepository.findAll(searchSpecification, pageable);
    }

    @Override
    public TopBooksDto getTopBooks(Integer limit) {
        SortRequest sortRequest = SortRequest.builder()
                .direction(SortDirection.DESC)
                .key("averageRating")
                .build();
        SearchRequest searchRequest = SearchRequest.builder()
                .sorts(List.of(sortRequest))
                .size(limit)
                .build();
        List<Book> topRateBooks = search(searchRequest).getContent();

        sortRequest.setKey("publishedDate");
        List<Book> topNewBooks = search(searchRequest).getContent();

        sortRequest.setKey("totalSold");
        List<Book> topSellBooks = search(searchRequest).getContent();

        return TopBooksDto.builder()
                .topRateBooks(topRateBooks)
                .topNewBooks(topNewBooks)
                .topSellBooks(topSellBooks)
                .build();
    }

    @Override
    public Book findBySlug(String slug) {
        Book book = bookRepository.findBySlug(slug);
        if (book == null) {
            throw BadRequestException.message("Book not found");
        }
        return book;
    }
}
