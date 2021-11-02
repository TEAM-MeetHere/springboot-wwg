package com.example.wherewego.controller;

import com.example.wherewego.domain.AddressObject;
import com.example.wherewego.domain.Member;
import com.example.wherewego.response.StatusCode;
import com.example.wherewego.security.JwtRequestFilter;
import com.example.wherewego.security.JwtTokenUtil;
import com.example.wherewego.service.JwtUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        final Member member = jwtUserDetailsService.authenticateByEmailAndPassword(
                authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final String token = jwtTokenUtil.generateToken(member.getEmail());
        return ResponseEntity.ok(new JwtResponse(
                StatusCode.OK,
                token,
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                member.getAddressObject()
        ));
    }
}

@Data
class JwtRequest {
    private String email;
    private String password;
}

@Data
@AllArgsConstructor
class JwtResponse {
    private int statusCode;
    private String token;
    private Long memberId;
    private String email;
    private String username;
    private String phone;
    private AddressObject addressObject;
}