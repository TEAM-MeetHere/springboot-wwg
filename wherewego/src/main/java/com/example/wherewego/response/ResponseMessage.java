package com.example.wherewego.response;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String UPDATE_USER_PASSWORD = "비밀번호가 갱신되었습니다";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";


    public static final String BOOKMARK_INSERT_SUCCESS = "즐겨찾기 저장 성공";
    public static final String BOOKMARK_SEARCH_SUCCESS = "즐겨찾기 조회 성공";
    public static final String UPDATE_BOOKMARK = "즐겨찾기 수정 성공";
    public static final String BOOKMARK_DELETE = "즐겨찾기 삭제 성공";

    public static final String RANDOMCODE_CREATED = "랜덤코드 생성 성공";
    public static final String RANCOMCODE_SEARCH_SUCCESS = "랜덤코드 내용 조회 성공";

    public static final String SEND_CODE = "인증번호 발송";
    public static final String VERIFY_FAIL = "올바른 인증번호가 아닙니다.";

    public static final String FRIEND_FIND = "친구 찾기 성공";
    public static final String FRIEND_SAVE = "친구 추가 성공";
    public static final String FRIEND_LIST = "친구 목록 불러오기 성공";
    public static final String FRIEND_DELETE = "회원 삭제 성공";
}
