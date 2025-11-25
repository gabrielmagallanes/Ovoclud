package com.ovocloud.proces_security.service;

import com.ovocloud.proces_security.dto.*;
import com.ovocloud.proces_security.entity.Usuario;
import com.ovocloud.proces_security.repository.UsuarioRepository;
import com.ovocloud.proces_security.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public BaseResponse authenticate(LoginRequest request) {

        var usuarioOpt = usuarioRepository.findByUsernameAndPassword(
                request.getUser(),
                request.getPassword()
        );

        if (usuarioOpt.isEmpty()) {
            return BaseResponse.builder()
                    .success(false)
                    .warning(false)
                    .errorCode(401)
                    .message("Credenciales inválidas")
                    .data(null)
                    .build();
        }

        Usuario usuario = usuarioOpt.get();

        if (!"A".equals(usuario.getEstado())) {
            return BaseResponse.builder()
                    .success(false)
                    .warning(true)
                    .errorCode(403)
                    .message("Usuario inactivo")
                    .data(null)
                    .build();
        }

        String token = jwtService.generateToken(
                usuario.getIdUsuario(),
                usuario.getIdEmpresa(),
                usuario.getIdPerfil()
        );
        String refreshToken = jwtService.generateRefreshToken(usuario.getIdUsuario());
        Long expireTime = jwtService.getExpirationTime();

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("refreshToken", refreshToken);
        data.put("typeToken", "Bearer");
        data.put("expireTimeToken", expireTime);
        data.put("idUser", usuario.getIdUsuario());
        data.put("idBussiness", usuario.getIdEmpresa());
        data.put("role", usuario.getIdPerfil());

        return BaseResponse.builder()
                .success(true)
                .warning(false)
                .errorCode(0)
                .message("OK")
                .data(data)
                .build();
    }

    public BaseResponse validateToken(ValidateTokenRequest request) {
        try {
            boolean isValid = jwtService.validateToken(request.getToken());

            if (isValid) {
                return BaseResponse.builder()
                        .success(true)
                        .warning(false)
                        .errorCode(0)
                        .message("OK")
                        .data(null)
                        .build();
            } else {
                return BaseResponse.builder()
                        .success(false)
                        .warning(false)
                        .errorCode(401)
                        .message("Token inválido o expirado")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return BaseResponse.builder()
                    .success(false)
                    .warning(false)
                    .errorCode(500)
                    .message("Error al validar token: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    public BaseResponse refreshToken(RefreshTokenRequest request) {
        try {
            boolean isValid = jwtService.validateToken(request.getRefreshtoken());

            if (!isValid) {
                return BaseResponse.builder()
                        .success(false)
                        .warning(false)
                        .errorCode(401)
                        .message("Refresh token inválido o expirado")
                        .data(null)
                        .build();
            }

            var usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

            if (usuarioOpt.isEmpty()) {
                return BaseResponse.builder()
                        .success(false)
                        .warning(false)
                        .errorCode(404)
                        .message("Usuario no encontrado")
                        .data(null)
                        .build();
            }

            Usuario usuario = usuarioOpt.get();


            if (!"A".equals(usuario.getEstado())) {
                return BaseResponse.builder()
                        .success(false)
                        .warning(true)
                        .errorCode(403)
                        .message("Usuario inactivo")
                        .data(null)
                        .build();
            }

            String newToken = jwtService.generateToken(
                    usuario.getIdUsuario(),
                    usuario.getIdEmpresa(),
                    usuario.getIdPerfil()
            );
            String newRefreshToken = jwtService.generateRefreshToken(usuario.getIdUsuario());
            Long expireTime = jwtService.getExpirationTime();

            Map<String, Object> data = new HashMap<>();
            data.put("token", newToken);
            data.put("refreshtoken", newRefreshToken);
            data.put("typeToken", "Bearer");
            data.put("expireTimeToken", expireTime);
            data.put("idUser", usuario.getIdUsuario());
            data.put("idBussiness", usuario.getIdEmpresa());
            data.put("role", usuario.getIdPerfil());

            return BaseResponse.builder()
                    .success(true)
                    .warning(false)
                    .errorCode(0)
                    .message("OK")
                    .data(data)
                    .build();

        } catch (Exception e) {
            return BaseResponse.builder()
                    .success(false)
                    .warning(false)
                    .errorCode(500)
                    .message("Error al refrescar token: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    public BaseResponse register(RegisterRequest request) {
        try {
            var existingUser = usuarioRepository.findByUsername(request.getUsername());

            if (existingUser.isPresent()) {
                return BaseResponse.builder()
                        .success(false)
                        .warning(true)
                        .errorCode(409)
                        .message("El usuario ya existe")
                        .data(null)
                        .build();
            }

            Usuario nuevoUsuario = Usuario.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .nombre(request.getNombre())
                    .idPerfil(request.getIdperfil())
                    .idEmpresa(request.getIdempresa())
                    .estado("A")
                    .build();

            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

            Map<String, Object> data = new HashMap<>();
            data.put("idUser", usuarioGuardado.getIdUsuario());
            data.put("username", usuarioGuardado.getUsername());
            data.put("nombre", usuarioGuardado.getNombre());

            return BaseResponse.builder()
                    .success(true)
                    .warning(false)
                    .errorCode(0)
                    .message("Usuario registrado exitosamente")
                    .data(data)
                    .build();

        } catch (Exception e) {
            return BaseResponse.builder()
                    .success(false)
                    .warning(false)
                    .errorCode(500)
                    .message("Error al registrar usuario: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }
}