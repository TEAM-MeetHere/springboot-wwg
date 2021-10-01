package com.example.wherewego.controller;

import com.example.wherewego.domain.Member;
import com.example.wherewego.dto.MemberDto;
import com.example.wherewego.dto.UpdateMemberDto;
import com.example.wherewego.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    //전체 회원 리스트
    @GetMapping("/memberList")
    public List<Member> memberList() {
        return memberService.findMembers();
    }

    //로그인
    @GetMapping("/members/login")
    public Member getMemberByEmail(@RequestParam String email, @RequestParam String pw) {
        Member member = memberService.findByEmail(email).get(0);
        memberService.checkPw(member.getPw(), pw);
        return member;
    }

    //회원가입
    @PostMapping("/members")
    public Member postMember(@RequestBody @Valid MemberDto memberDto) {
        return memberService.join(memberDto);
    }

    //아이디 찾기(이메일, 휴대전화)
    @PostMapping("/members/findId")
    public Member findMemberId(@RequestParam String email, @RequestParam String phone) {
        Member member = memberService.findByEmail(email).get(0);
        memberService.findByPhone(member, phone);
        return member;
    }

    //비밀번호 찾기(이메일, 이름, 휴대전화)
    @PostMapping("/members/findPw")
    public Member findMembersPw(@RequestParam String email, @RequestParam String name, @RequestParam String phone) {
        Member member = memberService.findByEmail(email).get(0);
        memberService.findByName(member, name);
        memberService.findByPhone(member, phone);
        return member;
    }

    //회원정보 수정
    @GetMapping("/members/update")
    public void updateMember(@RequestBody @Valid UpdateMemberDto updateMemberDto) {
        memberService.updateMember(updateMemberDto);
    }
}
