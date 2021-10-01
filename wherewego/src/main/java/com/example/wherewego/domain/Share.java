package com.example.wherewego.domain;

import com.example.wherewego.dto.ShareDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
        share.setCode(shareDto.getCode());
        share.setDestination(shareDto.getDestination());
        return share;
    }
}
