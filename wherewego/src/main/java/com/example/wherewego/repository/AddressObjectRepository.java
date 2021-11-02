package com.example.wherewego.repository;

import com.example.wherewego.domain.AddressObject;
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

}
