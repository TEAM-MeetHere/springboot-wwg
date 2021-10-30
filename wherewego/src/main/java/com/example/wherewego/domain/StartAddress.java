package com.example.wherewego.domain;

import com.example.wherewego.dto.StartAddressDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "START_ADDRESS")
public class StartAddress {

    @Id
    @GeneratedValue
    @Column(name = "ST_ID")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Bookmark bookmark;

    @Column(name = "ST_USERNAME")
    private String username;

    @Column(name = "ST_PLACE_NAME")
    private String placeName;

    @Column(name = "ST_ROAD_ADDRESS_NAME")
    private String roadAddressName;

    @Column(name = "ST_ADDRESS_NAME")
    private String addressName;

    @Column(name = "ST_LAT")
    private double lat;

    @Column(name = "ST_LON")
    private double lon;


    //== 연관관계 메서드 ==//
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
        bookmark.getStartAddressList().add(this);
    }

    //== 생성 메서드 ==//
    public static StartAddress createStartAddress(Bookmark bookmark, StartAddressDto startAddressDto) {
        StartAddress startAddress = new StartAddress();
        startAddress.setBookmark(bookmark);
        startAddress.setUsername(startAddressDto.getUsername());
        startAddress.setPlaceName(startAddressDto.getPlaceName());
        startAddress.setRoadAddressName(startAddressDto.getRoadAddressName());
        startAddress.setAddressName(startAddressDto.getAddressName());
        startAddress.setLat(startAddressDto.getLat());
        startAddress.setLon(startAddressDto.getLon());
        return startAddress;
    }
}
