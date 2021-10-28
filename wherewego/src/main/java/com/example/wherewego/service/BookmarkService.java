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

        Bookmark bookmark = Bookmark.createBookmark(member, bookmarkDto.getDestination(), bookmarkDto.getName(), bookmarkDto.getDate());
        bookmark.setMember(member);
        bookmarkRepository.save(bookmark);

        List<StartAddressDto> startAddressDtoList = bookmarkDto.getStartAddressDtoList();
        for (StartAddressDto startAddressDto : startAddressDtoList) {
            StartAddress startAddress = StartAddress.createStartAddress(bookmark, startAddressDto);
            startAddressRepository.save(startAddress);
        }

        return bookmark;
    }

    //해당 멤버의 즐겨찾기 목록
    @Transactional(readOnly = true)
    public List<Bookmark> findBookmarkList(Long memberId) {
        return bookmarkRepository.findByMemberId(memberId);
    }

    //즐겨찾기에 해당하는 출발 주소 리스트
    @Transactional(readOnly = true)
    public List<StartAddressDto> findStartAddress(Long bookmarkId) {
        List<StartAddress> allByBookmarkId = startAddressRepository.findAllByBookmarkId(bookmarkId);
        List<StartAddressDto> startAddressDtoList = new ArrayList<>();

        for (StartAddress startAddress : allByBookmarkId) {
            StartAddressDto startAddressDto = new StartAddressDto();
            startAddressDto.setName(startAddress.getName());
            startAddressDto.setAddress(startAddress.getAddress());
            startAddressDtoList.add(startAddressDto);
        }
        return startAddressDtoList;
    }
}
