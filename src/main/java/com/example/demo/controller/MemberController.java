package com.example.demo.controller;

import com.example.demo.domain.MemberForm;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RefreshTokenDTO;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.Response;
import com.example.demo.security.JwtProvider;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;


    /**
     * 회원 가입
     * @Param form 회원가입 form
     * @return json response
     */
    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(MemberForm form) {
        memberService.signUp(form);

        return Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("회원 가입 성공").build();
    }

    /**
     * 로그인
     * @Param loginDTO 로그인 요청 dto
     * @return json response
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUser_id(), loginDTO.getUser_pwd());

        //아이디 체크는 Authentication에 사용자 입력 아이디, 비밀번호를 입력해야됨
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info(authentication + " 로그인 처리 authentication");

        //jwt accessToken & refreshToken 발급
        String accessToken = jwtProvider.generateToken(authentication, false);
        String refreshToken = jwtProvider.generateToken(authentication, true);

        //회원 DB에 refreshToken 저장
        memberService.findMemberAndSaveRefreshToken(authentication.getName(), refreshToken);

        LoginResponse response = LoginResponse.builder()
                .status(HttpStatus.OK.value())
                .message("로그인 성공")
                .accessToken(accessToken)
                .expiredAt(LocalDateTime.now().plusSeconds(jwtProvider.getAccessTokenValidMilliSeconds()/1000))
                .refreshToken(refreshToken)
                .issuedAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * refreshToken으로 accessToken 재발급
     * @Param refreshTokenDTO accessToken 재발급 요청 DTO
     * @return json response
     */
    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        LoginResponse response = memberService.refreshToken(refreshTokenDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * 테스트
     * @return json response
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/test")
    public ResponseEntity test() {
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("테스트 성공")
                .build();
        return ResponseEntity.ok(response);
    }
}
