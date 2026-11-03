package com.wonik.hr.domain.member.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper 
public interface SnowflakeTestMapper {
    List<Map<String, String>> getCurrentUserInfo();
}
