package com.example.wherewego.service;

import com.example.wherewego.domain.AddressObject;
import com.example.wherewego.domain.Member;
import com.example.wherewego.dto.MemberDto;
import com.example.wherewego.dto.UpdateMemberDto;
import com.example.wherewego.exception.ApiRequestException;
import com.example.wherewego.repository.AddressObjectRepository;
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
    private final AddressObjectRepository addressObjectRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional //디비 적용
    public Member join(MemberDto memberDto, String activated, int code) {

        //중복 회원 검증, 중복 이메일 확인
        validateDuplicateMember(memberDto);

        //중복 회원 없다면
        Member member = Member.createMember(memberDto);
        String password = passwordEncoder.encode(member.getPw());
        member.setPw(password);
        member.setActivated(activated);
        member.setVerification(code);

        AddressObject addressObject = AddressObject.createAddressObject(
                member, memberDto.getAddressObject().getPlaceName(),
                memberDto.getAddressObject().getRoadAddressName(),
                memberDto.getAddressObject().getAddressName(),
                memberDto.getAddressObject().getLat(),
                memberDto.getAddressObject().getLon()
        );

        addressObjectRepository.save(addressObject);
        member.setAddressObject(addressObject);
        memberRepository.save(member);

        return member;
    }

    private void validateDuplicateMember(MemberDto memberDto) {
        //중복 이메일
        List<Member> findMembers = memberRepository.findByEmail(memberDto.getEmail());
        if (!findMembers.isEmpty() && findMembers.get(0).getActivated() == "TRUE") {
            throw new ApiRequestException("이미 존재하는 회원입니다.");
        }

        //중복 이름, 번호 (동명이인 존재할 수 있기 때문에 이름, 번호 동시에 확인)
        List<Member> findMembersByName = memberRepository.findByName(memberDto.getName());
        for (Member member : findMembersByName) {
            if (member.getPhone().equals(memberDto.getPhone())) {
                throw new ApiRequestException("이미 존재하는 회원입니다.");
            }
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

        //회원이 존재한다면
        return memberRepository.findByEmail(email);
    }

    //회원 탈퇴(아이디로 조회)
    @Transactional
    public void deleteMember(Long id) {
        Member member = findOne(id);
        addressObjectRepository.deleteAddressObject(member.getAddressObject().getId());
        memberRepository.deleteMember(id);
    }

    //로그인 비밀번호 확인
    public void checkPw(String pw, String memberPw) {
        if (!passwordEncoder.matches(memberPw, pw)) {
            throw new ApiRequestException("비밀번호가 일치하지 않습니다.");
        }
    }

    //회원 찾기(이름, 휴대전화)
    public Member findMemberByNameAndPhone(String name, String phone) {
        List<Member> memberList = memberRepository.findByName(name);
        if (memberList.isEmpty()) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다.");
        }
        for (Member member : memberList) {
            if (phone.equals(member.getPhone())) {
                return member;
            }
        }
        throw new ApiRequestException("휴대전화가 일치하지 않습니다.");
    }

    public Member findMemberPwByEmailAndNameAndPhone(String email, String name, String phone) {
        List<Member> memberList = memberRepository.findByEmail(email);
        if (memberList.isEmpty()) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다.");
        }
        Member member = memberList.get(0);
        return findMemberByNameAndPhone(name, phone);
    }

    //회원찾기(이름)
    public void findByName(Member member, String name) {
        String findName = member.getName();
        if (!findName.equals(name)) {
            throw new ApiRequestException("이름이 일치하지 않습니다.");
        }
    }

    //회원 정보 수정
    public Member updateMember(UpdateMemberDto updateMemberDto) {

        Long memberId = updateMemberDto.getMemberId();
        String pw1 = updateMemberDto.getPw1();
        String pw2 = updateMemberDto.getPw2();
        String name = updateMemberDto.getName();
        AddressObject address = updateMemberDto.getAddressObject();
        String phone = updateMemberDto.getPhone();

        if (!pw1.equals(pw2)) {
            throw new ApiRequestException("비밀번호가 일치하지 않습니다.");
        }

        Member member = memberRepository.findOne(memberId);

        if (member == null) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다.");
        }

        String password = passwordEncoder.encode(pw1);

        member.setPw(password);
        member.setName(name);
        member.setAddressObject(address);
        member.setPhone(phone);
        member.setAddressObject(address);

        address.setMember(member);

        return member;
    }

    //회원 활성화
    public boolean activateMember(String email, int code) {
        Member member = memberRepository.findByEmail(email).get(0);
        if (member.getVerification() == code) {
            member.setActivated("TRUE");
            return true;
        }
        return false;
    }
}
