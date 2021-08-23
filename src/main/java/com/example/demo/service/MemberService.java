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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


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

    /**
     * 로그인 요청 회원 찾기
     * @Param username 요청 아이디
     * @return 회원 정보 넣은 security User 객체
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("로그인 요청 회원 찾기");
        Member member = memberRepository.findMemberByUsernameFetch(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "아이디가 일치하지 않습니다."));

        return new User(member.getUsername(), member.getPassword(), authorities(member.getRoles()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<MemberRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    /**
     * 회원에게 refreshToken 저장
     * @Param username 요청 아이디
     * @Param refreshToken refreshToken 값
     */
    @Transactional
    public void findMemberAndSaveRefreshToken(String username, String refreshToken) {
        Member member = memberRepository.find
    }

}
