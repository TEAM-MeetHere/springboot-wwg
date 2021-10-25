package com.example.wherewego.service;

import com.example.wherewego.domain.Member;
import com.example.wherewego.exception.ApiRequestException;
import com.example.wherewego.exception.JwtRequestException;
import com.example.wherewego.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).get(0);
        if (member == null) {
            throw new ApiRequestException("해당 회원이 존재하지 않습니다. JwtUserDetailsService");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("TRUE"));

        return new User(member.getEmail(), member.getPw(), grantedAuthorities);
    }

    public Member authenticateByEmailAndPassword(String email, String password) {

        List<Member> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isEmpty()){
            throw new ApiRequestException("해당 회원이 존재하지 않습니다.");
        }

        Member member = byEmail.get(0);

        if (!passwordEncoder.matches(password, member.getPw())) {
            throw new JwtRequestException("비밀번호가 일치하지 않습니다");
//            throw new BadCredentialsException("비밀번호가 일치하지 않습니다. authenticate");
        }

        return member;
    }
}
