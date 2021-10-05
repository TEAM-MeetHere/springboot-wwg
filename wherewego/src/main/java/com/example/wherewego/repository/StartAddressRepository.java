package com.example.wherewego.repository;

import com.example.wherewego.domain.StartAddress;
import com.example.wherewego.dto.StartAddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StartAddressRepository {

    //엔티티 매니저 주입
    private final EntityManager em;

    //출발 주소 저장
    public void save(StartAddress startAddress) {
        em.persist(startAddress);
    }

    //아이디(PK)로 출발 주소 찾기
    public StartAddress findOne(Long id) {
        return em.find(StartAddress.class, id);
    }

    //즐찾 아이디로 출발 주소 불러오기
    public List<StartAddress> findAllByBookmarkId(Long bookmarkId) {
        return em.createQuery("select s from StartAddress s where s.bookmark.id =:bookmarkId")
                .setParameter("bookmarkId", bookmarkId)
                .getResultList();
    }
}
