package com.example.wherewego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateBookmarkDto {

    private Long bookmarkId;
    private String dateName;
    private String date;
}
