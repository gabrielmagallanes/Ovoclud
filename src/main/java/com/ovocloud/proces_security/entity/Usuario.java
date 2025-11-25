package com.ovocloud.proces_security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seg_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "idempresa")
    private Integer idEmpresa;

    @Column(name = "idperfil")
    private Integer idPerfil;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fchcrea")
    private LocalDateTime fchcrea;

    @Column(name = "usrcrea")
    private String usrcrea;

    @Column(name = "canalcrea")
    private String canalcrea;

    @Column(name = "fchmod")
    private LocalDateTime fchmod;

    @Column(name = "usrmod")
    private String usrmod;

    @Column(name = "canalmod")
    private String canalmod;

    @Column(name = "flgeli")
    private Boolean flgeli;
}