package com.example.wherewego.repository;

import com.example.wherewego.domain.AddressObject;
import com.example.wherewego.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AddressObjectRepository {

    //엔티티 매니저 주입
    private final EntityManager em;

    //주소 저장
    public void save(AddressObject addressObject) {
        em.persist(addressObject);
    }

    //주소 삭제
    public void deleteAddressObject(Long id) {
        AddressObject addressObject = em.find(AddressObject.class, id);
        em.remove(addressObject);
    }

}
