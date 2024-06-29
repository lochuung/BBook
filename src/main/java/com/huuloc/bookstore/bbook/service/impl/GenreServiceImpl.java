package com.huuloc.bookstore.bbook.service.impl;

import com.google.gson.JsonObject;
import com.huuloc.bookstore.bbook.entity.Genre;
import com.huuloc.bookstore.bbook.repository.GenreRepository;
import com.huuloc.bookstore.bbook.service.GenreService;
import com.nimbusds.jose.util.IOUtils;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private JSONParser jsonParser;

    @Transactional
    @Override
    public void createData() throws IOException, ParseException {
        if (genreRepository.count() > 0) {
            return;
        }
        InputStream inputStream = getClass().getResourceAsStream("/data/genres.json");
        assert inputStream != null;
        JSONObject jsonObject = (JSONObject) jsonParser.parse(IOUtils.readInputStreamToString(inputStream,
                StandardCharsets.UTF_8));
        JSONArray genres = (JSONArray) jsonObject.get("genres");
        List<Genre> genreList = new ArrayList<>();

        genres.forEach(genre -> {
            JSONObject genreJson = (JSONObject) genre;
            Genre genreEntity = Genre.builder()
                    .name((String) genreJson.get("name"))
                    .slug((String) genreJson.get("slug"))
                    .build();
            genreList.add(genreEntity);
        });

        genreRepository.saveAll(genreList);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
}
