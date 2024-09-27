package com.kucw.security.controller;

import com.kucw.security.dto.MemberRegisterRequest;
import com.kucw.security.model.member.Member;
import com.kucw.security.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("members/register")
    public ResponseEntity<Member> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {

        Member newMember = memberService.register(memberRegisterRequest);

        ResponseEntity<Member> member = ResponseEntity.status(HttpStatus.CREATED).body(newMember);

        return member;
    }


}
