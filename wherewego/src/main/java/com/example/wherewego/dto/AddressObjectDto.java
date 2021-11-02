package com.example.wherewego.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AddressObjectDto {

    private String placeName;
    private String roadAddressName;
    private String addressName;
    private double lat;
    private double lon;
}
