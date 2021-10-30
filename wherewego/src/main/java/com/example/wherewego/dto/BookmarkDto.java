package com.example.wherewego.dto;

import com.example.wherewego.domain.StartAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookmarkDto {

    private Long memberId;
    private String placeName;
    private List<StartAddressDto> startAddressList;
    private String username;
    private String dateName;
    private String roadAddressName;
    private String addressName;
    private double lat;
    private double lon;
    private String date;

}
