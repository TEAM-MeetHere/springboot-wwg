package com.example.wherewego.repository;

import com.example.wherewego.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


//스프링 빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //엔티티 매니저 주입
    private final EntityManager em;

    //회원 저장
    public void save(Member member) {
        em.persist(member);
    }

    //아이디(PK)로 회원 찾기
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //회원 목록
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //이름으로 회원 찾기
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name=:name")
                .setParameter("name", name)
                .getResultList();
    }

    //이메일로 회원찾기
    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email=:email")
                .setParameter("email", email)
                .getResultList();
    }

    //회원 탈퇴
    public void deleteMember(Long id) {
        Member member = em.find(Member.class, id);
        em.remove(member);
    }
}
