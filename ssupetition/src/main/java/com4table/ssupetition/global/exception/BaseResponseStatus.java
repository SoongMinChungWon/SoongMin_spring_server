package com4table.ssupetition.global.exception;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    // -------- 성공 코드 시작 -------- //
    SUCCESS(true, 1000, "요청에 성공했습니다."),
    NOT_EXISTS_LOGIN_ID(true, 2000, "해당 아이디를 사용할 수 있습니다."),
    SATISFIED_PW_CRITERIA(true, 2001, "비밀번호가 확인되었습니다."),
    SEND_EMAIL(true, 2002, "인증번호를 전송했습니다."),
    // MainController[회원가입(3)]에서 2002번 status 생성하여 사용, code 겹치지 않기 위해 작성

    EQUAL_CERTIFICATION_NUM(true, 2003, "인증번호가 일치합니다."),
    SIGN_UP_COMPLETE(true, 2004, "회원가입을 완료하였습니다."),

    FOLLOW_COMPLETE(true, 3000,"팔로우 완료"),





    // -------- 성공 코드 종료 -------- //



    // -------- 실패 코드 시작 -------- //
    // -------- 필요한 에러 코드 추가 => Code 만들 때 안겹치게 몇번대 사용할 건지 얘기할 것  -------- //
    /**
     * User
     * Code : 2000
     */



    INVALID_PASSWORD(false, 2000, "아이디 또는 비밀번호가 틀렸습니다."),
    ALREADY_EXISTS(false, 20001, "아이디가 이미 존재합니다."),
    ENTITY_NOT_FOUND(false, 2002,"entity not found"),
    USER_NOT_FOUND(false, 2003, "해당하는 유저 정보가 없습니다."),
    EMPTY_TOKEN(false, 2004, "토큰을 확인해주세요."),
    ALREADY_FOLLOWING(false, 3100, "이미 팔로잉 중입니다."),
    NOT_FOLLOWING(false, 3101, "해당 유저를 팔로잉하지 않았습니다."),
    NOT_FOLLOWER(false, 3102, "해당 유저는 팔로워가 아닙니다."),


    POST_NOT_FOUND(false, 404, "해당 게시글이 존재하지 않습니다."),
    PLAYLIST_NOT_FOUND(false, 404, "해당 플레이리스트가 존재하지 않습니다."),
    POST_IS_NOT_ON_PLAYLIST(false, 400, "게시글은 해당 플레이리스트에 담겨 있지 않습니다."),


    NOT_EQUAL_PW(false, 2101, "비밀번호가 일치하지 않습니다."),
    NOT_SATISFIED_PW_CRITERIA(false, 2102, "영어(대/소문), 숫자, 특수문자를 포함해주세요."),

    UNABLE_TO_SEND_EMAIL(false, 2103, "이메일 전송에 실패하였습니다."),
    NOT_EQUAL_CERTIFICATION_NUM(false, 2104, "인증번호가 일치하지 않습니다."),

    UPLOAD_FAIL(false,404,"업로드 실패"),



    ;


    // -------- 실패 코드 종료 -------- //

    private boolean isSuccess; // 성공 여부
    private String message; // 메시지
    private int code; // 코드
    /**
     * BaseResponseStatus 에서 해당하는 코드를 매핑
     *
     * @param isSuccess
     * @param code
     * @param message
     */

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}