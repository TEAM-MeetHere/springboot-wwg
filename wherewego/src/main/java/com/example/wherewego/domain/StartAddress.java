package com.example.wherewego.domain;

import com.example.wherewego.dto.StartAddressDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "ST_NAME")
    private String name;

    @Column(name = "ST_ADDRESS")
    private String address;

    //== 연관관계 메서드 ==//
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
        bookmark.getStartAddressList().add(this);
    }

    //== 생성 메서드 ==//
    public static StartAddress createStartAddress(Bookmark bookmark, StartAddressDto startAddressDto) {
        StartAddress startAddress = new StartAddress();
        startAddress.setBookmark(bookmark);
        startAddress.setName(startAddressDto.getName());
        startAddress.setAddress(startAddressDto.getAddress());
        return startAddress;
    }
}
