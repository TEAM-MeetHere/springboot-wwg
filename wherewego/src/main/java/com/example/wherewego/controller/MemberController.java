package com.example.wherewego.controller;

import com.example.wherewego.domain.Bookmark;
import com.example.wherewego.domain.Friend;
import com.example.wherewego.domain.Member;
import com.example.wherewego.domain.Share;
import com.example.wherewego.dto.*;
import com.example.wherewego.exception.ApiRequestException;
import com.example.wherewego.exception.ErrorResponse;
import com.example.wherewego.response.DefaultRes;
import com.example.wherewego.response.ResponseMessage;
import com.example.wherewego.response.StatusCode;
import com.example.wherewego.service.*;
import com.example.wherewego.valid.ValidationSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final FriendService friendService;
    private final BookmarkService bookmarkService;
    private final ShareService shareService;

    //전체 회원 리스트
    @GetMapping("/memberList")
    public List<Member> memberList() {
        return memberService.findMembers();
    }

    //로그인
    @PostMapping("/members/login")
    public ResponseEntity getMemberByEmail(@Validated
                                           @RequestBody LoginDto loginDto,
                                           BindingResult result) {

        ResponseEntity errorResponse = checkBindingResultError(result);
        if (errorResponse != null) return errorResponse;

        Member member = memberService.findByEmail(loginDto.getEmail()).get(0);

        if (member == null || member.getActivated().equals("FALSE")) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다.");
        }

        memberService.checkPw(member.getPw(), loginDto.getPw());
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.LOGIN_SUCCESS, member), HttpStatus.OK);
    }

    //회원가입 과정
    @PostMapping("/members")
    public ResponseEntity postMember(@Validated(ValidationSequence.class)
                                     @RequestBody MemberDto memberDto,
                                     BindingResult result) {

        ResponseEntity errorResponse = checkBindingResultError(result);
        if (errorResponse != null) return errorResponse;

        List<Member> findMembers = memberService.findByEmail(memberDto.getEmail());

        //기존 이메일이 db에 존재한다면
        if (!findMembers.isEmpty()) {
            //인증 절차 진행 안된 이메일이라면, 이전 이메일 삭제
            if (findMembers.get(0).getActivated().equals("FALSE")) {
                memberService.deleteMember(findMembers.get(0).getId());
            }
            //인증 절차 완료된 이메일 이라면, 해당 요청 에러 응답 (이미 존재하는 회원)
            else {
                throw new ApiRequestException("이미 존재하는 회원입니다.");
            }
        }

        Random random = new Random(System.currentTimeMillis());
        int code = 100000 + random.nextInt(900000);
        String active = "FALSE";

        Member member = memberService.join(memberDto, active, code);

        MailDto mailDto = emailService.verification(memberDto.getEmail(), memberDto.getName(), member.getVerification());
        emailService.mailSend(mailDto);

        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,
                ResponseMessage.SEND_CODE, member), HttpStatus.CREATED);
    }

    //회원가입 인증
    @PostMapping("/members/verify")
    public ResponseEntity register(@RequestBody VerifyDto verifyDto) {
        String email = verifyDto.getEmail();
        int code = verifyDto.getCode();
        if (memberService.activateMember(email, code)) {
            return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,
                    ResponseMessage.CREATED_USER, "회원가입 완료"), HttpStatus.CREATED);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST,
                ResponseMessage.VERIFY_FAIL, "인증 오류"), HttpStatus.OK);
    }

    //아이디 찾기(이름, 휴대전화)
    @GetMapping("/members/findId")
    public ResponseEntity findMemberId(@RequestParam String name,
                                       @RequestParam String phone) {
        String findEmail = memberService.findMemberByNameAndPhone(name, phone).getEmail();
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.READ_USER, findEmail), HttpStatus.OK);
    }

    //비밀번호 찾기(이메일, 이름, 휴대전화)
    @GetMapping("/members/findPw")
    public ResponseEntity findMembersPw(@RequestParam String email,
                                        @RequestParam String name,
                                        @RequestParam String phone) {
        Member member = memberService.findMemberPwByEmailAndNameAndPhone(email, name, phone);
        String tempPw = "qwe123!@#";
        String tempMessage = "비밀번호가 재설정되었습니다. 임시 비밀번호는 이메일로 전송되었습니다.";
        MailDto mailDto = emailService.createMailAndChangePassword(email, name, tempPw);
        emailService.mailSend(mailDto);


        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.UPDATE_USER_PASSWORD, tempMessage), HttpStatus.OK);
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

        Member member = memberService.findOne(memberId);
        //회원 관련 모든 데이터 삭제

        //1. 친구 리스트 삭제
        List<Friend> friendList = friendService.findFriendList(memberId);
        for (Friend friend : friendList) {
            friendService.deleteFriend(friend.getId());
        }

        //다른 회원의 친구목록에서도 탈퇴한 회원 삭제 필요
        List<Friend> findMemberByEmail = friendService.findMemberInFriendListByEmail(member.getEmail());
        for (Friend friend : findMemberByEmail) {
            friendService.deleteFriend(friend.getId());
        }


        //2. 북마크 리스트 삭제
        List<Bookmark> bookmarkList = bookmarkService.findBookmarkList(memberId);
        for (Bookmark bookmark : bookmarkList) {
            bookmarkService.deleteBookmark(bookmark.getId());
        }

        //3. 공유 리스트 삭제
        List<Share> shareList = shareService.findShareByUsername(member.getName());
        for (Share share : shareList) {
            shareService.deleteShare(share.getId());
        }

        memberService.deleteMember(memberId);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.DELETE_USER, "회원 탈퇴 성공"), HttpStatus.OK);
    }

    //친구 찾기
    @GetMapping("/friend/find")
    public ResponseEntity findFriend(@RequestParam String email, String name, String phone) {
        Long friendId = friendService.findFriend(email, name, phone);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.FRIEND_FIND, friendId), HttpStatus.OK);
    }

    //친구 추가
    @PostMapping("/friend/save")
    public ResponseEntity postFriend(@RequestParam Long memberId, Long friendId) {
        Friend friend = friendService.save(memberId, friendId);
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,
                ResponseMessage.FRIEND_SAVE, friend), HttpStatus.OK);
    }

    //회원의 친구 목록
    @GetMapping("/friend/list")
    public ResponseEntity friendList(@RequestParam Long memberId) {
        List<Friend> friendList = friendService.findFriendList(memberId);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.FRIEND_LIST, friendList), HttpStatus.OK);
    }

    //친구 삭제
    @DeleteMapping("/friend/delete")
    public ResponseEntity deleteFriend(@RequestParam Long friendId) {
        friendService.deleteFriend(friendId);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.FRIEND_DELETE, "친구 삭제 성공"), HttpStatus.OK);
    }

    //valid 에 어긋난 에러 체크
    private ResponseEntity checkBindingResultError(BindingResult result) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(result.getFieldError().getDefaultMessage());
            errorResponse.setStatusCode(StatusCode.BAD_REQUEST);
            return new ResponseEntity(errorResponse, HttpStatus.OK);
        }
        return null;
    }
}
