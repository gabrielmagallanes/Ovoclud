package com.ovocloud.proces_security.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateTokenRequest {
    private String token;
}