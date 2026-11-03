package com.wonik.hr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class HrApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void snowflakeConnectionTest() {
        var result = jdbcTemplate.queryForMap("""
            SELECT CURRENT_USER(), CURRENT_ACCOUNT(), CURRENT_VERSION()
            """);

        System.out.println(result);
    }

	@Test
	void contextLoads() {
	}


}
