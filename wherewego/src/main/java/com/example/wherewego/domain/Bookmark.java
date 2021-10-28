package com.example.wherewego.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEM_ID")
    private Member member;

    @Column(name = "BOOK_DESTINATION")
    private String destination;

    @Column(name = "BOOK_NAME")
    private String name;

    @Column(name = "BOOK_DATE")
    private Date date;

    @JsonIgnore
    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StartAddress> startAddressList = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Bookmark createBookmark(Member member, String destination, String name, Date date) {
        Bookmark bookmark = new Bookmark();
        bookmark.setMember(member);
        bookmark.setDestination(destination);
        bookmark.setName(name);
        bookmark.setDate(date);
        return bookmark;
    }

    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.member = member;
        member.getBookmarkList().add(this);
    }
}
