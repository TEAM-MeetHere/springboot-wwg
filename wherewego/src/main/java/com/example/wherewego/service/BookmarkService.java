package com.example.wherewego.service;

import com.example.wherewego.domain.Bookmark;
import com.example.wherewego.domain.Member;
import com.example.wherewego.domain.StartAddress;
import com.example.wherewego.dto.BookmarkDto;
import com.example.wherewego.dto.StartAddressDto;
import com.example.wherewego.repository.BookmarkRepository;
import com.example.wherewego.repository.MemberRepository;
import com.example.wherewego.repository.StartAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional //트랜잭션, 영속성 컨텍스트
public class BookmarkService {

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final StartAddressRepository startAddressRepository;

    //즐겨찾기 저장
    public Bookmark save(BookmarkDto bookmarkDto) {
        Long memberId = bookmarkDto.getMemberId();
        Member member = memberRepository.findOne(memberId);

        Bookmark bookmark = Bookmark.createBookmark(member,
               bookmarkDto.getPlaceName(), bookmarkDto.getUsername(),
                bookmarkDto.getDateName(), bookmarkDto.getRoadAddressName(),
                bookmarkDto.getAddressName(), bookmarkDto.getLat(),
                bookmarkDto.getLon(), bookmarkDto.getDate());
        bookmark.setMember(member);
        bookmarkRepository.save(bookmark);

        List<StartAddressDto> startAddressList = bookmarkDto.getStartAddressList();
        for (StartAddressDto startAddressDto : startAddressList) {
            StartAddress startAddress = StartAddress.createStartAddress(bookmark, startAddressDto);
            startAddressRepository.save(startAddress);
        }

        return bookmark;
    }

    //해당 멤버의 즐겨찾기 목록
    @Transactional(readOnly = true)
    public List<Bookmark> findBookmarkList(Long memberId) {
        return bookmarkRepository.findBookmarkListByMemberId(memberId);
    }

    //즐겨찾기에 해당하는 출발 주소 리스트
    @Transactional(readOnly = true)
    public List<StartAddressDto> findStartAddress(Long bookmarkId) {
        List<StartAddress> allByBookmarkId = startAddressRepository.findAllByBookmarkId(bookmarkId);
        List<StartAddressDto> startAddressDtoList = new ArrayList<>();

        for (StartAddress startAddress : allByBookmarkId) {
            StartAddressDto startAddressDto = new StartAddressDto();
            startAddressDto.setPlaceName(startAddress.getPlaceName());
            startAddressDto.setUsername(startAddress.getUsername());
            startAddressDto.setRoadAddressName(startAddress.getRoadAddressName());
            startAddressDto.setAddressName(startAddress.getAddressName());
            startAddressDto.setLat(startAddress.getLat());
            startAddressDto.setLon(startAddress.getLon());
            startAddressDtoList.add(startAddressDto);
        }
        return startAddressDtoList;
    }

    //즐겨찾기 삭제
    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        List<StartAddress> startAddressList = startAddressRepository.findAllByBookmarkId(bookmarkId);
        for (StartAddress startAddress : startAddressList) {
            startAddressRepository.deleteStartAddress(startAddress.getId());
        }
        bookmarkRepository.deleteBookmark(bookmarkId);
    }
}
