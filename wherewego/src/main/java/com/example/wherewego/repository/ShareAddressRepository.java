package com.example.wherewego.repository;

import com.example.wherewego.domain.ShareAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShareAddressRepository {

    //엔티티 매니저 주입
    private final EntityManager em;

    //공유 주소 저장
    public void save(ShareAddress shareAddress) {
        em.persist(shareAddress);
    }

    //아이디(PK)로 공유 주소 찾기
    public List<ShareAddress> findOne() {
        return em.createQuery("select s from ShareAddress s", ShareAddress.class)
                .getResultList();
    }

    //공유코드로 공유주소 불러오기
    public List<ShareAddress> findByShareCode(String shareCode){
        return em.createQuery("select s from ShareAddress s where s.share.code =:sharedCode")
                .setParameter("shareCode", shareCode)
                .getResultList();
    }
}
