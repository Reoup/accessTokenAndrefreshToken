package com.example.demo.mapper;

import com.example.demo.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    MemberDTO loginCheck(MemberDTO memberDTO);
}
