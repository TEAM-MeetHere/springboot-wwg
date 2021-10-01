package com.example.wherewego.repository;

import com.example.wherewego.domain.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkRepository {

    //엔티티 매니저 주입
    private final EntityManager em;

    //즐찾 저장
    public void save(Bookmark bookmark) {
        em.persist(bookmark);
    }

    //아이디(PK)로 즐찾 찾기
    public Bookmark findOne(Long id) {
        return em.find(Bookmark.class, id);
    }

    //즐찾 목록
    public List<Bookmark> findAll() {
        return em.createQuery("select b from Bookmark b", Bookmark.class)
                .getResultList();
    }

    //회원 아이디로 즐찾 정보 찾기
    public List<Bookmark> findByMemberId(Long memberId) {
        return em.createQuery("select b from Bookmark b where b.member.id =:memberId", Bookmark.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
