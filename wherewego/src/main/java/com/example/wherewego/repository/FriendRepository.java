package com.example.wherewego.repository;

import com.example.wherewego.domain.Friend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendRepository {

    //엔티티 매니저 주입
    private final EntityManager em;

    //친구 저장
    public void save(Friend friend) {
        em.persist(friend);
    }

    //해당 회원의 친구 목록
    public List<Friend> findFriendListByMemberId(Long memberId) {
        return em.createQuery("select f from Friend f where f.member.id =:memberId")
                .setParameter("memberId", memberId)
                .getResultList();
    }

    //이메일로 친구 찾기
    public List<Friend> findMemberInFriendListByEmail(String email) {
        return em.createQuery("select f from Friend f where f.email =:email")
                .setParameter("email", email)
                .getResultList();
    }

    //친구 삭제
    public void deleteFriend(Long id) {
        Friend friend = em.find(Friend.class, id);
        em.remove(friend);
    }
}
