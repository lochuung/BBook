package com.huuloc.bookstore.bbook.service;

import com.huuloc.bookstore.bbook.entity.Genre;
import net.minidev.json.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface GenreService {
    void createData() throws IOException, ParseException, ParseException;

    List<Genre> findAll();
}
