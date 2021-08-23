package com.example.demo.mapper;

import com.example.demo.dto.LoginDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    LoginDTO loginCheck(LoginDTO memberDTO);
}
