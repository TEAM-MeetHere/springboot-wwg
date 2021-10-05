package com.example.wherewego.service;

import com.example.wherewego.domain.Share;
import com.example.wherewego.domain.ShareAddress;
import com.example.wherewego.dto.ShareAddressDto;
import com.example.wherewego.dto.ShareDto;
import com.example.wherewego.exception.ApiRequestException;
import com.example.wherewego.repository.ShareAddressRepository;
import com.example.wherewego.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional //트랜잭션, 영속성 컨텍스트
public class ShareService {

    private final ShareRepository shareRepository;
    private final ShareAddressRepository shareAddressRepository;

    //공유코드 저장
    public Share save(ShareDto shareDto) {
        Share share = Share.createShare(shareDto);

        //랜덤코드 중복 여부 확인
        while (!isCodeUnique(share)) {
            share = Share.createShare(shareDto);
        }

        shareRepository.save(share);

        List<ShareAddressDto> shareAddressDtoList = shareDto.getShareAddressDtoList();
        for (ShareAddressDto shareAddressDto : shareAddressDtoList) {
            ShareAddress shareAddress = ShareAddress.createShareAddress(share, shareAddressDto);
            shareAddressRepository.save(shareAddress);
        }

        return share;
    }

    //공유 코드에 해당하는 출발 주소 리스트
    @Transactional(readOnly = true)
    public List<ShareAddressDto> findShareList(String shareCode){
        List<ShareAddress> byShareCode = shareAddressRepository.findByShareCode(shareCode);
        if (byShareCode.isEmpty()) {
            throw new ApiRequestException("잘못된 랜덤코드 입니다.");
        }

        List<ShareAddressDto> shareAddressDtoList = new ArrayList<>();

        for (ShareAddress shareAddress : byShareCode) {
            ShareAddressDto shareAddressDto = new ShareAddressDto();
            shareAddressDto.setName(shareAddress.getName());
            shareAddressDto.setAddress(shareAddress.getAddress());
            shareAddressDtoList.add(shareAddressDto);
        }

        return shareAddressDtoList;
    }

    private boolean isCodeUnique(Share share) {
        List<Share> shareByRandomCode = shareRepository.findShareByRandomCode(share.getCode());
        if (!shareByRandomCode.isEmpty()) {
            return false;
        }
        return true;
    }
}
