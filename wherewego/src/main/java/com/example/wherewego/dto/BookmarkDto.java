package com.example.wherewego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookmarkDto {

    private Long memberId;
    private String destination;
    private List<StartAddressDto> startAddressDtoList;
    private String name;
    private Date date;

}
