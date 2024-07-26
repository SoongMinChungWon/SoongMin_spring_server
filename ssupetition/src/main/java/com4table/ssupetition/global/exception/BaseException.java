package com4table.ssupetition.global.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    public BaseResponseStatus status;


    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
    }
}

