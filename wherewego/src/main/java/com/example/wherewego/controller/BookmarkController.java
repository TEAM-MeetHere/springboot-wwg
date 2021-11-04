package com.example.wherewego.controller;

import com.example.wherewego.domain.Bookmark;
import com.example.wherewego.dto.BookmarkDto;
import com.example.wherewego.dto.StartAddressDto;
import com.example.wherewego.exception.ErrorResponse;
import com.example.wherewego.response.DefaultRes;
import com.example.wherewego.response.ResponseMessage;
import com.example.wherewego.response.StatusCode;
import com.example.wherewego.service.BookmarkService;
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
public class BookmarkController {

    private final BookmarkService bookmarkService;

    //즐겨찾기 저장
    @PostMapping("/bookmark/save")
    public ResponseEntity postBookmark(@Validated @RequestBody BookmarkDto bookmarkDto,
                                       BindingResult result) {

        ResponseEntity errorResponse = checkBindingResultError(result);
        if (errorResponse != null) return errorResponse;

        Bookmark bookmark = bookmarkService.save(bookmarkDto);
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,
                ResponseMessage.BOOKMARK_INSERT_SUCCESS, bookmark), HttpStatus.OK);
    }

    //회원의 즐겨찾기 목록
    @GetMapping("/bookmark/list")
    public ResponseEntity bookmarkList(@RequestParam Long memberId) {
        List<Bookmark> bookmarkList = bookmarkService.findBookmarkList(memberId);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.BOOKMARK_SEARCH_SUCCESS, bookmarkList), HttpStatus.OK);
    }

    //즐겨찾기 불러오기
    @GetMapping("/bookmark/load")
    public ResponseEntity bookmarkAddressList(@RequestParam Long bookmarkId) {
        List<StartAddressDto> startAddressDtoList = bookmarkService.findStartAddress(bookmarkId);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,
                ResponseMessage.BOOKMARK_SEARCH_SUCCESS, startAddressDtoList), HttpStatus.OK);
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
