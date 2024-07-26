package com4table.ssupetition.global.exception;

import org.springframework.stereotype.Component;

@Component
public class BaseResponseServiceImpl implements BaseResponseService {

    /**
     * 성공 응답 메서드 - 전달 데이터 O
     *
     * @param data - 결과 데이터
     * @param <T>  - 반환 타입 => Generic
     * @return BaseResponse - 응답 객체
     */
    public <T> BaseResponse<Object> getSuccessResponse(T data) {
        return BaseResponse.builder()
                .isSuccess(true)
                .code(BaseResponseStatus.SUCCESS.getCode())
                .message(BaseResponseStatus.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    /**
     * 성공 응답 메서드 - 전달 데이터 X
     *
     * @param <T> - 반환 타입 => Generic
     * @return BaseResponse - 응답 객체
     */
    public <T> BaseResponse<Object> getSuccessResponse() {
        return BaseResponse.builder()
                .isSuccess(true)
                .code(BaseResponseStatus.SUCCESS.getCode())
                .message(BaseResponseStatus.SUCCESS.getMessage())
                .build();
    }

    public <T> BaseResponse<Object> getSuccessResponse(BaseResponseStatus status) {
        return BaseResponse.builder()
                .isSuccess(status.isSuccess())
                .code(status.getCode())
                .message(status.getMessage())
                .build();
    }



    /**
     * 실패 응답 메서드
     *
     * @param status - BaseResponseStatus에서 생성한 status
     * @param <T>    - 반환 타입 => Generic
     * @return BaseResponse - 응답 객체
     */
    public <T> BaseResponse<Object> getFailureResponse(BaseResponseStatus status) {
        return BaseResponse.builder()
                .isSuccess(status.isSuccess())
                .code(status.getCode())
                .message(status.getMessage())
                .build();
    }
}