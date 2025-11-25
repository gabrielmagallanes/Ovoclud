package com.ovocloud.proces_security.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginRequest {
    private String user;
    private String password;
    private String channelCode;
}
