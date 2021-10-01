package com.example.wherewego.repository;

import com.example.wherewego.domain.Share;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShareRepository {

    //엔티티 매니저 주입
    private final EntityManager em;

    //공유코드 저장
    public void save(Share share){
        em.persist(share);
    }

    //아이디(PK)로 공유코드 찾기
    public Share findOne(Long id){
        return em.find(Share.class, id);
    }

    //공유코드 목록
    public List<Share> findAll() {
        return em.createQuery("select s from Share s", Share.class)
                .getResultList();
    }

    //
}
