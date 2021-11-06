package com.example.wherewego.service;

import com.example.wherewego.domain.Friend;
import com.example.wherewego.domain.Member;
import com.example.wherewego.dto.FriendDto;
import com.example.wherewego.exception.ApiRequestException;
import com.example.wherewego.repository.FriendRepository;
import com.example.wherewego.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional //트랜잭션, 영속성 컨텍스트
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    //친구 찾기
    public Long findFriend(String email, String name, String phone) {
        List<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isEmpty()) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다1");
        }
        Member member = findMember.get(0);
        if (!member.getName().equals(name)) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다2");
        }
        if (!member.getPhone().equals(phone)) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다3");
        }
        return member.getId();
    }

    //친구 저장
    public Friend save(Long memberId, Long friendId) {
        Member findMember = memberRepository.findOne(friendId);
        if (findMember == null) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다");
        }

        FriendDto friendDto = new FriendDto();
        friendDto.setEmail(findMember.getEmail());
        friendDto.setName(findMember.getName());
        friendDto.setPhone(findMember.getPhone());

        Member member = memberRepository.findOne(memberId);

        Friend friend = Friend.createFriend(
                member, friendDto.getEmail(), friendDto.getName(), friendDto.getPhone());
        friend.setMember(member);
        friendRepository.save(friend);
        return friend;
    }

    //해당 멤버의 친구 목록
    @Transactional(readOnly = true)
    public List<Friend> findFriendList(Long memberId) {
        return friendRepository.findFriendListByMemberId(memberId);
    }

    //이메일로 회원찾기
    @Transactional(readOnly = true)
    public List<Friend> findMemberInFriendListByEmail(String email) {
        return friendRepository.findMemberInFriendListByEmail(email);
    }

    //친구 삭제
    @Transactional
    public void deleteFriend(Long friendId) {
        friendRepository.deleteFriend(friendId);
    }
}
