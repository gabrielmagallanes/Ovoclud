package com.ovocloud.proces_security.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer statusCode;
    private String timestamp;
    private String userMessage;
    private List<ErrorDetail> errors;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDetail {
        private String errorCode;
        private String message;
        private String url;
    }

    public static ErrorResponse create(Integer statusCode, String userMessage, String errorCode, String message, String url) {
        String currentTimestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        return ErrorResponse.builder()
                .statusCode(statusCode)
                .timestamp(currentTimestamp)
                .userMessage(userMessage)
                .errors(List.of(
                        ErrorDetail.builder()
                                .errorCode(errorCode)
                                .message(message)
                                .url(url)
                                .build()
                ))
                .build();
    }
}