package com.example.wherewego.domain;

import com.example.wherewego.dto.MemberDto;
import com.example.wherewego.exception.ApiRequestException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@Table(name = "MEMBER")
@AllArgsConstructor
@NoArgsConstructor
public class Member extends Timestamped {

    @Id
    @GeneratedValue
    @Column(name = "MEM_ID")
    private Long id;

    @Column(name = "MEM_EMAIL", unique = true)
    private String email;

    @Column(name = "MEM_PW")
    private String pw;

    @Column(name = "MEM_NAME")
    private String name;

    @Column(name = "MEM_ADDRESS")
    private String address;

    @Column(name = "MEM_PHONE")
    private String phone;

    @Column(name = "MEM_VERIFICATION")
    private int verification;

    @Column(name = "MEM_ACTIVATED")
    private String activated;

    @Column(name = "MEM_SNS_TYPE")
    private String snsType;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    /*@ManyToOne
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "MEM_ID", referencedColumnName = "MEM_ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTH_NAME", referencedColumnName = "AUTH_NAME")})
    private Set<Authority> authorities;*/

    //== 생성 메서드==//
    public static Member createMember(MemberDto memberDto) {

        //입력값 검증
        validateMemberDto(memberDto);

        Member member = new Member();
        member.setEmail(memberDto.getEmail());
        member.setPw(memberDto.getPw());
        member.setName(memberDto.getName());
        member.setAddress(memberDto.getAddress());
        member.setPhone(memberDto.getPhone());
        member.setSnsType(memberDto.getSnsType());
        return member;
    }

    public static void validateMemberDto(MemberDto memberDto) {
        if (memberDto.getEmail() == null || memberDto.getName().isEmpty()) {
            throw new ApiRequestException("이메일은 필수 입력 값입니다.");
        }
        if (memberDto.getPw() == null || memberDto.getPw().isEmpty()) {
            throw new ApiRequestException("비밀번호는 필수 입력 값입니다.");
        }
        if (memberDto.getName() == null || memberDto.getName().isEmpty()) {
            throw new ApiRequestException("이름은 필수 입력 값입니다.");
        }
        if (memberDto.getAddress() == null || memberDto.getAddress().isEmpty()) {
            throw new ApiRequestException("주소는 필수 입력 값입니다.");
        }
        if (memberDto.getPhone() == null || memberDto.getPhone().isEmpty()) {
            throw new ApiRequestException("휴대전화는 필수 입력 값입니다.");
        }
    }
}
