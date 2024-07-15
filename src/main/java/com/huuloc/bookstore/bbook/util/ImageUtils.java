package com.huuloc.bookstore.bbook.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@UtilityClass
@Slf4j
public class ImageUtils {

    public static byte[] getBytesFromUrl(String url) throws MalformedURLException {
        URL imageUrl = new URL(url);
        InputStream is = null;
        byte[] bytes = null;
        try {
            is = imageUrl.openStream();
            bytes = is.readAllBytes();
        } catch (Exception e) {
            log.error("Error when get bytes from url: {}", e.getMessage());
        }
        return bytes;
    }
}
