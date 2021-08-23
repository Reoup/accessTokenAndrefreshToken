package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.MemberForm;
import com.example.demo.domain.MemberRole;
import com.example.demo.dto.MemberDTO;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * 회원 가입
     * @Param form 회원가입 form
     */
    @Transactional
    public void signUp(MemberForm form) {
        Member member = Member.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .name(form.getName())
                .roles(Set.of(MemberRole.USER, MemberRole.ADMIN))
                .build();
    }

}
