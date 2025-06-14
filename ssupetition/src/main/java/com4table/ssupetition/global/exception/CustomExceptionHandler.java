package com4table.ssupetition.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<Object>> handleBaseException(BaseException e) {
        BaseResponseStatus status = e.getStatus();
        BaseResponse<Object> response = BaseResponse.builder()
                .isSuccess(status.isSuccess())
                .code(status.getCode())
                .message(status.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(status.getCode()));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<?>> handleIllegalException(IllegalArgumentException e) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
            .isSuccess(false)
            .code(400)
            .message(e.getMessage())
            .build());
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<BaseResponse<Object>> handleGeneralException(Exception e) {
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        BaseResponse<Object> response = BaseResponse.builder()
//                .isSuccess(false) // HttpStatus에는 isSuccess() 메서드가 없으므로 직접 설정
//                .code(status.value()) // HttpStatus의 상태 코드
//                .message(status.getReasonPhrase()) // HttpStatus의 이유 구절
//                .build();
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        StringBuilder message = new StringBuilder();

        for (FieldError error : result.getFieldErrors()) {
            message.append("[")
                    .append(error.getField())
                    .append("] ")
                    .append(":")
                    .append(error.getDefaultMessage());
        }

        BaseResponse<Object> response = BaseResponse.builder()
                .isSuccess(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message.toString())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
}
