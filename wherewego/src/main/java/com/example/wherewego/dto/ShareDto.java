package com.example.wherewego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShareDto {

    private String placeName;
    private String username;
    private String roadAddressName;
    private String addressName;
    private double lat;
    private double lon;
    private List<ShareAddressDto> shareAddressDtoList;
}
