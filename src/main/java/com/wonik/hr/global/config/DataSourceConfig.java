package com.wonik.hr.global.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

@Configuration 
@EnableTransactionManagement
@MapperScan(
        basePackages = "com.wonik.hr.**.mapper",
        sqlSessionFactoryRef = "sqlSessionFactory"
)
public class DataSourceConfig {
/**
     * 1. Snowflake Hikari DataSource 생성
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.snowflake.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 2. MyBatis SqlSessionFactory 설정
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dataSource") DataSource dataSource,
            ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // Mapper XML 파일 위치 지정 (src/main/resources/mapper/ 하위 모든 xml)
        factoryBean.setMapperLocations(
                applicationContext.getResources("classpath*:mapper/**/*.xml")
        );

        // DTO/VO 클래스 패키지 별칭(Alias) 자동 등록 (XML에서 패키지명 생략 가능)
        factoryBean.setTypeAliasesPackage("com.company.project.**.dto, com.company.project.**.vo");

        // MyBatis 세부 옵션 설정 (Snowflake 호환 최적화)
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); // DB의 SNAKE_CASE 컬럼을 Java의 camelCase 필드로 자동 매핑
        configuration.setJdbcTypeForNull(JdbcType.NULL); // 파라미터에 null 전달 시 Snowflake 타입 에러 방지
        configuration.setCallSettersOnNulls(true);       // 조회 결과 컬럼이 null이어도 Map/DTO에 키/필드 유지
        configuration.setDefaultStatementTimeout(60);    // 쿼리 실행 타임아웃 (60초)
        
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

    /**
     * 3. SqlSessionTemplate 생성
     */
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 4. 트랜잭션 매니저 설정
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
