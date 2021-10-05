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

    @Column(name = "SH_DESTINATION")
    private String destination;

    @JsonIgnore
    @OneToMany(mappedBy = "share", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ShareAddress> shareAddressList = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Share createShare(ShareDto shareDto) {
        Share share = new Share();
        share.setDestination(shareDto.getDestination());

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
