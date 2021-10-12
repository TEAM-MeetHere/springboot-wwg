package com.example.wherewego.service;


import com.example.wherewego.domain.Member;
import com.example.wherewego.dto.MailDto;
import com.example.wherewego.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@AllArgsConstructor
public class EmailService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "YOUR_EMAIL_ADDRESS";

    public MailDto createMailAndChangePassword(String userEmail, String userName, String tempPw) {

        MailDto mailDto = new MailDto();
        mailDto.setAddress(userEmail);
        mailDto.setTitle(userName + "님의 임시 비밀번호 안내 이메일 입니다.");
        mailDto.setMessage("안녕하세요. 회원님의 임시 비밀번호는 [" + tempPw + "] 입니다.");
        updatePw(tempPw, userEmail);
        return mailDto;
    }

    public void updatePw(String str, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail).get(0);
        String pw = passwordEncoder.encode(str);
        member.setPw(pw);
    }

    public MailDto verification(String userEmail, String userName, int code) {

        MailDto mailDto = new MailDto();
        mailDto.setAddress(userEmail);
        mailDto.setTitle(userName + "님의 회원가입 인증 번호 입니다.");
        mailDto.setMessage("안녕하세요 회원가입 인증 번호는 [" + code + "] 입니다");
        return mailDto;
    }




    public void mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(EmailService.FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }

}
