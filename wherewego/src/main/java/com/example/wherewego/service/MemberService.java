package com.example.wherewego.service;

import com.example.wherewego.domain.Member;
import com.example.wherewego.dto.MemberDto;
import com.example.wherewego.dto.UpdateMemberDto;
import com.example.wherewego.exception.ApiRequestException;
import com.example.wherewego.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional //트랜잭션, 영속성 컨텍스트
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional //디비 적용
    public Member join(MemberDto memberDto) {

        //중복 회원 검증, 중복 이메일 확인
        validateDuplicateMember(memberDto);

        //중복 회원 없다면
        Member member = Member.createMember(memberDto);
        String password = passwordEncoder.encode(member.getPw());
        member.setPw(password);
        memberRepository.save(member);
        return member;
    }

    private void validateDuplicateMember(MemberDto memberDto) {
        List<Member> findMembers = memberRepository.findByEmail(memberDto.getEmail());
        if (!findMembers.isEmpty()) {
            throw new ApiRequestException("이미 존재하는 회원입니다.");
        }
    }

    //전체 회원 조회
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //아이디로 회원 조회
    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    //이메일로 회원 조회
    @Transactional(readOnly = true)
    public List<Member> findByEmail(String email) {

        //회원 존재 여부 확인
        validateNotExistMember(email);

        //회원이 존재한다면
        return memberRepository.findByEmail(email);
    }

    private void validateNotExistMember(String email) {
        List<Member> findMembers = memberRepository.findByEmail(email);
        if (findMembers.isEmpty()) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다.");
        }
    }

    //회원 탈퇴(아이디로 조회)
    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteMember(id);
    }

    //로그인 비밀번호 확인
    public void checkPw(String pw, String memberPw) {
        if (!passwordEncoder.matches(memberPw, pw)) {
            throw new ApiRequestException("비밀번호가 일치하지 않습니다.");
        }
    }

    //회원 찾기(이메일, 휴대전화)
    public void findByPhone(Member member, String phone) {
        String findPhone = member.getPhone();
        if (!findPhone.equals(phone)) {
            throw new ApiRequestException("휴대전화가 일치하지 않습니다.");
        }
    }

    //회원찾기(이름)
    public void findByName(Member member, String name) {
        String findName = member.getName();
        if (!findName.equals(name)) {
            throw new ApiRequestException("이름이 일치하지 않습니다.");
        }
    }

    //회원 정보 수정
    public void updateMember(UpdateMemberDto updateMemberDto) {

        Long memberId = updateMemberDto.getMemberId();
        String pw1 = updateMemberDto.getPw1();
        String pw2 = updateMemberDto.getPw2();
        String name = updateMemberDto.getName();
        String address = updateMemberDto.getAddress();
        String phone = updateMemberDto.getPhone();

        if (!pw1.equals(pw2)) {
            throw new ApiRequestException("비밀번호가 일치하지 않습니다.");
        }

        Member member = memberRepository.findOne(memberId);
        String password = passwordEncoder.encode(pw1);

        member.setPw(password);
        member.setName(name);
        member.setAddress(address);
        member.setPw(phone);
    }
}
