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

    //공유코드로 목록 확인
    public List<Share> findShareByRandomCode(String randomCode){
        return em.createQuery("select s from Share s where s.code =:randomCode")
                .setParameter("randomCode", randomCode)
                .getResultList();
    }

    //회원 이름으로 공유코드 확인
    public List<Share> findShareByUsername(String username) {
        return em.createQuery("select s from Share s where s.username =:username")
                .setParameter("username", username)
                .getResultList();
    }

    //공유코드 삭제
    public void deleteShare(Long id) {
        Share share = em.find(Share.class, id);
        em.remove(share);
    }
}
