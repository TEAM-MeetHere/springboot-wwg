package com.example.wherewego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateMemberDto {

    private Long memberId;
    private String pw1;
    private String pw2;
    private String name;
    private String address;
    private String phone;
}
