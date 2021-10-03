package com.example.wherewego.controller;

import com.example.wherewego.domain.Member;
import com.example.wherewego.dto.LoginDto;
import com.example.wherewego.dto.MemberDto;
import com.example.wherewego.dto.UpdateMemberDto;
import com.example.wherewego.response.DefaultRes;
import com.example.wherewego.response.ResponseMessage;
import com.example.wherewego.response.StatusCode;
import com.example.wherewego.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
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
    public ResponseEntity getMemberByEmail(@RequestBody LoginDto loginDto) {
        Member member = memberService.findByEmail(loginDto.getEmail()).get(0);
        memberService.checkPw(member.getPw(), loginDto.getPw());
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.LOGIN_SUCCESS, loginDto), HttpStatus.OK);
    }

    //회원가입
    @PostMapping("/members")
    public ResponseEntity postMember(@RequestBody @Valid MemberDto memberDto, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.join(memberDto);
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,
                ResponseMessage.CREATED_USER, memberDto), HttpStatus.CREATED);
    }

    //아이디 찾기(이름, 휴대전화)
    @GetMapping("/members/findId")
    public ResponseEntity findMemberId(@RequestParam String name, @RequestParam String phone) {
        String findEmail = memberService.findMemberByNameAndPhone(name, phone).getEmail();
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.READ_USER, findEmail), HttpStatus.FOUND);
    }

    //비밀번호 찾기(이메일, 이름, 휴대전화)
    @GetMapping("/members/findPw")
    public ResponseEntity findMembersPw(@RequestParam String email, @RequestParam String name, @RequestParam String phone) {
        Member member = memberService.findMemberPwByEmailAndNameAndPhone(email, name, phone);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.READ_USER, member), HttpStatus.OK);
    }

    //회원정보 수정
    @PostMapping("/members/update")
    public void updateMember(@RequestBody @Valid UpdateMemberDto updateMemberDto) {
        memberService.updateMember(updateMemberDto);
    }

    //회원 탈퇴
    @DeleteMapping("/members/delete")
    public void deleteMember(@RequestParam Long memberId) {
        memberService.deleteMember(memberId);
    }
}
