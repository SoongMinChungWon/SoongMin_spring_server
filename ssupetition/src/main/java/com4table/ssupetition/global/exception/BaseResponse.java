package com4table.ssupetition.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseResponse<T> {

    private boolean isSuccess; // 성공, 실패 여부
    private String message; // 메시지
    private int code; // 코드
    private T data; // 전달 데이터

    // data 타입을 String으로 바꾼 생성자
    public BaseResponse(boolean isSuccess, String message, int code, String data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.code = code;
        this.data = (T) data;
    }

    @Builder
    public BaseResponse(boolean isSuccess, String message, int code, T data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.code = code;
        this.data = data;
    }
}