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
    @JoinColumn(name = "SH_ID")
    private Share share;

    @Column(name = "SA_NAME")
    private String name;

    @Column(name = "SA_ADDRESS")
    private String address;

    //== 생성 메서드 ==//
    public static ShareAddress createShareAddress(Share share, ShareAddressDto shareAddressDto) {
        ShareAddress shareAddress = new ShareAddress();
        shareAddress.setShare(share);
        shareAddress.setName(shareAddressDto.getName());
        shareAddress.setAddress(shareAddressDto.getAddress());
        return shareAddress;
    }

    //== 연관관계 메서드 ==//
    public void setShare(Share share) {
        this.share = share;
        share.getShareAddressList().add(this);
    }
}
