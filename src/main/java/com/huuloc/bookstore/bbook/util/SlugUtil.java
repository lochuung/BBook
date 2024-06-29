package com.huuloc.bookstore.bbook.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SlugUtil {
    public static String toSlug(String input) {
        // Vietnamese to English
        input = input.replaceAll("đ", "d");
        input = input.replaceAll("Đ", "D");
        input = input.replaceAll("á|à|ả|ã|ạ|ă|ắ|ằ|ẳ|ẵ|ặ|â|ấ|ầ|ẩ|ẫ|ậ", "a");
        input = input.replaceAll("Á|À|Ả|Ã|Ạ|Ă|Ắ|Ằ|Ẳ|Ẵ|Ặ|Â|Ấ|Ầ|Ẩ|Ẫ|Ậ", "A");
        input = input.replaceAll("é|è|ẻ|ẽ|ẹ|ê|ế|ề|ể|ễ|ệ", "e");
        input = input.replaceAll("É|È|Ẻ|Ẽ|Ẹ|Ê|Ế|Ề|Ể|Ễ|Ệ", "E");
        input = input.replaceAll("í|ì|ỉ|ĩ|ị", "i");
        input = input.replaceAll("Í|Ì|Ỉ|Ĩ|Ị", "I");
        input = input.replaceAll("ó|ò|ỏ|õ|ọ|ô|ố|ồ|ổ|ỗ|ộ|ơ|ớ|ờ|ở|ỡ|ợ", "o");
        input = input.replaceAll("Ó|Ò|Ỏ|Õ|Ọ|Ô|Ố|Ồ|Ổ|Ỗ|Ộ|Ơ|Ớ|Ờ|Ở|Ỡ|Ợ", "O");
        input = input.replaceAll("ú|ù|ủ|ũ|ụ|ư|ứ|ừ|ử|ữ|ự", "u");
        input = input.replaceAll("Ú|Ù|Ủ|Ũ|Ụ|Ư|Ứ|Ừ|Ử|Ữ|Ự", "U");
        input = input.replaceAll("ý|ỳ|ỷ|ỹ|ỵ", "y");
        input = input.replaceAll("Ý|Ỳ|Ỷ|Ỹ|Ỵ", "Y");
        return input.toLowerCase().trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");
    }
}
