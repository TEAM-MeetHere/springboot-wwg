package com.example.wherewego.controller;

import com.example.wherewego.domain.Share;
import com.example.wherewego.dto.ShareAddressDto;
import com.example.wherewego.dto.ShareDto;
import com.example.wherewego.exception.ErrorResponse;
import com.example.wherewego.response.DefaultRes;
import com.example.wherewego.response.ResponseMessage;
import com.example.wherewego.response.StatusCode;
import com.example.wherewego.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShareController {

    private final ShareService shareService;

    //공유코드 저장
    @PostMapping("/share/save")
    public ResponseEntity postShare(@Validated @RequestBody ShareDto shareDto,
                                    BindingResult result) {

        ResponseEntity errorResponse = checkBindingResultError(result);
        if (errorResponse != null) return errorResponse;

        Share share = shareService.save(shareDto);
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,
                ResponseMessage.RANDOMCODE_CREATED, share), HttpStatus.CREATED);
    }

    //공유코드 내용 불러오기
    @GetMapping("/share/load")
    public ResponseEntity shareAddressList(@RequestParam String code) {
        List<ShareAddressDto> shareAddressList = shareService.findShareList(code);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.RANCOMCODE_SEARCH_SUCCESS, shareAddressList), HttpStatus.FOUND);
    }

    //valid 에 어긋난 에러 체크
    private ResponseEntity checkBindingResultError(BindingResult result) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(result.getFieldError().getDefaultMessage());
            errorResponse.setStatusCode(StatusCode.BAD_REQUEST);
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
