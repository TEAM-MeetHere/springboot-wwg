package com.example.wherewego.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "BOOKMARK")
public class Bookmark extends Timestamped {

    @Id
    @GeneratedValue
    @Column(name = "BOOK_ID")
    private Long id;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEM_ID")
    private Member member;

    @Column(name = "BOOK_DESTINATION")
    private String placeName;

    @Column(name = "BOOK_NAME")
    private String username;

    @Column(name = "BOOK_DATE_NAME")
    private String dateName;

    @Column(name = "BOOK_ROAD_ADDRESS_NAME")
    private String roadAddressName;

    @Column(name = "BOOK_ADDRESS_NAME")
    private String addressName;

    @Column(name = "BOOK_LAT")
    private double lat; //위도

    @Column(name = "BOOK_LON")
    private double lon; //경도

    @Column(name = "BOOK_DATE")
    private String date;

//    @JsonIgnore
    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StartAddress> startAddressList = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Bookmark createBookmark(Member member,
            String placeName, String username, String dateName, String roadAddressName, String addressName, double lat, double lon, String date) {
        Bookmark bookmark = new Bookmark();
        bookmark.setMember(member);
        bookmark.setPlaceName(placeName);
        bookmark.setUsername(username);
        bookmark.setDateName(dateName);
        bookmark.setRoadAddressName(roadAddressName);
        bookmark.setAddressName(addressName);
        bookmark.setLat(lat);
        bookmark.setLon(lon);
        bookmark.setDate(date);
        return bookmark;
    }

    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.member = member;
        member.getBookmarkList().add(this);
    }
}
