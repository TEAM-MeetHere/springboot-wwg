package com.example.wherewego.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "FRIEND")
@AllArgsConstructor
@NoArgsConstructor
public class Friend extends Timestamped {

    @Id
    @GeneratedValue
    @Column(name = "FRI_ID")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEM_ID")
    private Member member;

    @Column(name = "FRI_EMAIL")
    private String email;

    @Column(name = "FRI_NAME")
    private String name;

    @Column(name = "FRI_PHONE")
    private String phone;

    //== 생성 메서드 ==//
    public static Friend createFriend(Member member,
        String email, String name, String phone) {
        Friend friend = new Friend();
        friend.setMember(member);
        friend.setEmail(email);
        friend.setName(name);
        friend.setPhone(phone);
        return friend;
    }

    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.member = member;
        member.getFriendList().add(this);
    }
}
