package com.wonik.hr;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wonik.hr.domain.member.mapper.SnowflakeTestMapper;

@SpringBootTest
class HrApplicationTests {

    // @Autowired
    // @Qualifier("snowflakeDataSource")
    // DataSource snowflakeDataSource;

    @Autowired
    SnowflakeTestMapper snowflakeTestMapper;

    @Test
    void mybatisSnowflakeConnectionTest() {
        var result = snowflakeTestMapper.getCurrentUserInfo();
        System.out.println(result);
    }

    // @Test
    // void snowflakeConnectionTest() {
    //     JdbcTemplate jdbcTemplate = new JdbcTemplate(/* snowflakeDataSource */);

    //     var result = jdbcTemplate.queryForMap("""
    //         SELECT CURRENT_USER(), CURRENT_ACCOUNT(), CURRENT_VERSION()
    //         """);

    //     System.out.println(result);
    // }

	@Test
	void contextLoads() {
	}


}
