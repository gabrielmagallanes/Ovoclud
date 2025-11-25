package com.ovocloud.proces_security.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String nombre;
    private Integer idperfil;
    private Integer idempresa;
}