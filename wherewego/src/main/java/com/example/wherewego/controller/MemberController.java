package com.example.wherewego.controller;

import com.example.wherewego.domain.Member;
import com.example.wherewego.dto.LoginDto;
import com.example.wherewego.dto.MemberDto;
import com.example.wherewego.dto.UpdateMemberDto;
import com.example.wherewego.exception.ErrorResponse;
import com.example.wherewego.response.DefaultRes;
import com.example.wherewego.response.ResponseMessage;
import com.example.wherewego.response.StatusCode;
import com.example.wherewego.service.MemberService;
import com.example.wherewego.valid.ValidationGroups;
import com.example.wherewego.valid.ValidationSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity getMemberByEmail(@Validated
                                               @RequestBody LoginDto loginDto,
                                           BindingResult result) {

        ResponseEntity errorResponse = checkBindingResultError(result);
        if (errorResponse != null) return errorResponse;

        Member member = memberService.findByEmail(loginDto.getEmail()).get(0);
        memberService.checkPw(member.getPw(), loginDto.getPw());
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.LOGIN_SUCCESS, member), HttpStatus.OK);
    }

    //회원가입
    @PostMapping("/members")
    public ResponseEntity postMember(@Validated(ValidationSequence.class)
                                         @RequestBody MemberDto memberDto,
                                     BindingResult result) {

        ResponseEntity errorResponse = checkBindingResultError(result);
        if (errorResponse != null) return errorResponse;

        Member member = memberService.join(memberDto);
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,
                ResponseMessage.CREATED_USER, memberDto), HttpStatus.CREATED);
    }

    //아이디 찾기(이름, 휴대전화)
    @GetMapping("/members/findId")
    public ResponseEntity findMemberId(@RequestParam String name,
                                       @RequestParam String phone) {
        String findEmail = memberService.findMemberByNameAndPhone(name, phone).getEmail();
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.READ_USER, findEmail), HttpStatus.FOUND);
    }

    //비밀번호 찾기(이메일, 이름, 휴대전화)
    @GetMapping("/members/findPw")
    public ResponseEntity findMembersPw(@RequestParam String email,
                                        @RequestParam String name,
                                        @RequestParam String phone) {
        Member member = memberService.findMemberPwByEmailAndNameAndPhone(email, name, phone);
        String tempPw = "qwe123!@#";
        String tempMessage = tempPw + " 로 임시 비밀번호가 설정되었습니다.";
        member.setPw(tempPw);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.UPDATE_USER, tempMessage), HttpStatus.OK);
    }

    //회원정보 수정
    @PostMapping("/members/update")
    public ResponseEntity updateMember(@Validated(ValidationSequence.class)
                                           @RequestBody UpdateMemberDto updateMemberDto,
                                       BindingResult result) {
        ResponseEntity errorResponse = checkBindingResultError(result);
        if (errorResponse != null) return errorResponse;
        Member member = memberService.updateMember(updateMemberDto);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.UPDATE_USER, member), HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping("/members/delete")
    public ResponseEntity deleteMember(@RequestParam Long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.DELETE_USER, "회원 탈퇴 성공"), HttpStatus.OK);
    }

    //valid 에 어긋난 에러 체크
    private ResponseEntity checkBindingResultError(BindingResult result) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(result.getFieldError().getDefaultMessage());
            errorResponse.setStatusCode(StatusCode.BAD_REQUEST);
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
