package com.huuloc.bookstore.bbook.controller.api;

import com.huuloc.bookstore.bbook.dto.filter.SearchRequest;
import com.huuloc.bookstore.bbook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(bookService.search(searchRequest));
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTop(@RequestParam(required = false, defaultValue = "10")
                                        Integer limit) {
        return ResponseEntity.ok(bookService.getTopBooks(limit));
    }
}
