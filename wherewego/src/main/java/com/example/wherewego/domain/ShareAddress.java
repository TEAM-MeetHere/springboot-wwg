package com.example.wherewego.domain;

import com.example.wherewego.dto.ShareAddressDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "SHARE_ADDRESS")
public class ShareAddress {

    @Id
    @GeneratedValue
    @Column(name = "SA_ID")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SA_SHARE")
    private Share share;

    @Column(name = "SA_PLACE_NAME")
    private String placeName;

    @Column(name = "SA_USERNAME")
    private String username;

    @Column(name = "SA_ROAD_ADDRESS_NAME")
    private String roadAddressName;

    @Column(name = "SA_ADDRESS_NAME")
    private String addressName;

    @Column(name = "SA_LAT")
    private double lat;

    @Column(name = "SA_LON")
    private double lon;

    //== 생성 메서드 ==//
    public static ShareAddress createShareAddress(Share share, ShareAddressDto shareAddressDto) {
        ShareAddress shareAddress = new ShareAddress();
        shareAddress.setShare(share);
        shareAddress.setPlaceName(shareAddressDto.getPlaceName());
        shareAddress.setUsername(shareAddressDto.getUsername());
        shareAddress.setRoadAddressName(shareAddressDto.getRoadAddressName());
        shareAddress.setAddressName(shareAddressDto.getAddressName());
        shareAddress.setLat(shareAddressDto.getLat());
        shareAddress.setLon(shareAddressDto.getLon());
        return shareAddress;
    }

    //== 연관관계 메서드 ==//
    public void setShare(Share share) {
        this.share = share;
        share.getShareAddressList().add(this);
    }
}
