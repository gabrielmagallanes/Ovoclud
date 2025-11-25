package com.ovocloud.proces_security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private boolean success;
    private boolean warning;
    private Integer errorCode;
    private String message;
    private Object data;
}