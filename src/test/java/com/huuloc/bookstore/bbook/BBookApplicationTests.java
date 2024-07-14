package com.huuloc.bookstore.bbook;

import com.huuloc.bookstore.bbook.util.SlugUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BBookApplicationTests {

	@Test
	void contextLoads() {
		String result = SlugUtils.toSlug("Đây là sách");
		assert result.equals("day-la-sach");
	}

}
