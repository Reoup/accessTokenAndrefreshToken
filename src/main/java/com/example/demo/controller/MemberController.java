package com.example.demo.controller;

import com.example.demo.domain.MemberForm;
import com.example.demo.dto.response.Response;
import com.example.demo.security.JwtProvider;
import com.example.demo.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class MemberController {

    private final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private AuthenticationManager authenticationManager;

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

    }






}
