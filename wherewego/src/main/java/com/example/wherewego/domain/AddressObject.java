package com.example.wherewego.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "ADDRESS_OBJECT")
@AllArgsConstructor
@NoArgsConstructor
public class AddressObject {

    @Id
    @GeneratedValue
    @Column(name = "AO_ID")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "AO_PLACE_NAME")
    private String placeName;

    @Column(name = "AO_ROAD_ADDRESS_NAME")
    private String roadAddressName;

    @Column(name = "AO_ADDRESS_NAME")
    private String addressName;

    @Column(name = "AO_LAT")
    private double lat;

    @Column(name = "AO_LON")
    private double lon;

    //== 생성 메서드 ==//
    public static AddressObject createAddressObject(
            Member member, String placeName, String roadAddressName,
            String addressName, double lat, double lon) {
        AddressObject addressObject = new AddressObject();
        addressObject.setMember(member);
        addressObject.setPlaceName(placeName);
        addressObject.setRoadAddressName(roadAddressName);
        addressObject.setAddressName(addressName);
        addressObject.setLat(lat);
        addressObject.setLon(lon);
        return addressObject;
    }

    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.member = member;
    }
}
