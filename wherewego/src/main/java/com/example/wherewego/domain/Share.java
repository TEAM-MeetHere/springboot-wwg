package com.example.wherewego.domain;

import com.example.wherewego.dto.ShareDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
@Table(name = "SHARE")
public class Share extends Timestamped {

    @Id
    @GeneratedValue
    @Column(name = "SH_ID")
    private Long id;

    @Column(name = "SH_CODE")
    private String code;

    @Column(name = "SH_PLACENAME")
    private String placeName;

    @Column(name = "SH_USERNAME")
    private String username;

    @Column(name = "SH_ROAD_ADDRESS_NAME")
    private String roadAddressName;

    @Column(name = "SH_ADDRESS_NAME")
    private String addressName;

    @Column(name = "SH_LAT")
    private double lat;

    @Column(name = "SH_LON")
    private double lon;

    @JsonIgnore
    @OneToMany(mappedBy = "share", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ShareAddress> shareAddressList = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Share createShare(ShareDto shareDto) {
        Share share = new Share();
        share.setPlaceName(shareDto.getPlaceName());
        share.setUsername(shareDto.getUsername());
        share.setRoadAddressName(shareDto.getRoadAddressName());
        share.setAddressName(shareDto.getAddressName());
        share.setLat(shareDto.getLat());
        share.setLon(shareDto.getLon());

        //랜덤코드 생성 후, 적용
        String randomCode = makeRandomCode();
        share.setCode(randomCode);

        return share;
    }

    private static String makeRandomCode() {
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        rnd.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 20; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }
}
