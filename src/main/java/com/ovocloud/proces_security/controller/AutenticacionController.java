package com.ovocloud.proces_security.controller;

import com.ovocloud.proces_security.dto.*;
import com.ovocloud.proces_security.service.AutenticacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/system/v1/operation/operation-gateway/session")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AutenticacionController {

    private final AutenticacionService autenticacionService;


    @PostMapping("/auth")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        try {
            // Validar campos requeridos (después de que Spring ya parseó el JSON correctamente)
            if (request.getUser() == null || request.getPassword() == null) {
                ErrorResponse error = ErrorResponse.create(
                        400,
                        "La solicitud contiene datos inválidos o incompletos. Verifique los campos requeridos.",
                        "E400",
                        "Bad Request - Missing required fields.",
                        httpRequest.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            BaseResponse response = autenticacionService.authenticate(request);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(response.getErrorCode()).body(response);
            }

        } catch (org.springframework.dao.DataAccessException e) {
            // Error de base de datos (tabla no existe, datos inválidos en BD, etc.)
            ErrorResponse error = ErrorResponse.create(
                    500,
                    "El servidor encontró una condición inesperada que le impidió cumplir con la solicitud",
                    "E307",
                    "An unexpected error occurred on the server.",
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            // Cualquier otra excepción no controlada
            ErrorResponse error = ErrorResponse.create(
                    500,
                    "El servidor encontró una condición inesperada que le impidió cumplir con la solicitud",
                    "E307",
                    "An unexpected error occurred on the server.",
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(
            @RequestBody ValidateTokenRequest request,
            HttpServletRequest httpRequest) {
        try {
            if (request.getToken() == null) {
                ErrorResponse error = ErrorResponse.create(
                        400,
                        "La solicitud contiene datos inválidos o incompletos. Verifique los campos requeridos.",
                        "E400",
                        "Bad Request - Missing required fields.",
                        httpRequest.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            BaseResponse response = autenticacionService.validateToken(request);

            if (!response.isSuccess() && response.getErrorCode() == 401) {
                ErrorResponse error = ErrorResponse.create(
                        401,
                        "Se requiere autenticación para acceder a este recurso. Proporcione un token de acceso válido.",
                        "E302",
                        "Auth Token not found or not authorized.",
                        httpRequest.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(response.getErrorCode()).body(response);
            }

        } catch (org.springframework.dao.DataAccessException e) {
            ErrorResponse error = ErrorResponse.create(
                    500,
                    "El servidor encontró una condición inesperada que le impidió cumplir con la solicitud",
                    "E307",
                    "An unexpected error occurred on the server.",
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            ErrorResponse error = ErrorResponse.create(
                    500,
                    "El servidor encontró una condición inesperada que le impidió cumplir con la solicitud",
                    "E307",
                    "An unexpected error occurred on the server.",
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> refreshToken(
            @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest) {
        try {
            if (request.getRefreshtoken() == null || request.getUsername() == null) {
                ErrorResponse error = ErrorResponse.create(
                        400,
                        "La solicitud contiene datos inválidos o incompletos. Verifique los campos requeridos.",
                        "E400",
                        "Bad Request - Missing required fields.",
                        httpRequest.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            BaseResponse response = autenticacionService.refreshToken(request);

            if (!response.isSuccess() && response.getErrorCode() == 401) {
                ErrorResponse error = ErrorResponse.create(
                        401,
                        "Se requiere autenticación para acceder a este recurso. Proporcione un token de acceso válido.",
                        "E302",
                        "Auth Token not found or not authorized.",
                        httpRequest.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(response.getErrorCode()).body(response);
            }

        } catch (org.springframework.dao.DataAccessException e) {
            ErrorResponse error = ErrorResponse.create(
                    500,
                    "El servidor encontró una condición inesperada que le impidió cumplir con la solicitud",
                    "E307",
                    "An unexpected error occurred on the server.",
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            ErrorResponse error = ErrorResponse.create(
                    500,
                    "El servidor encontró una condición inesperada que le impidió cumplir con la solicitud",
                    "E307",
                    "An unexpected error occurred on the server.",
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck(HttpServletRequest httpRequest) {
        try {
            BaseResponse response = BaseResponse.builder()
                    .success(true)
                    .warning(false)
                    .errorCode(0)
                    .message("Service is up and running")
                    .data(null)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorResponse error = ErrorResponse.create(
                    503,
                    "El servicio de dependencia ascendente no responde. Inténtelo de nuevo más tarde.",
                    "E308",
                    "The service is temporarily unavailable.",
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
    }

    // Este manejador captura cuando el Content-Type no es application/json (ej: text/plain)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException e,
            HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.create(
                415,
                "La entidad de solicitud tiene un tipo de medio que el servidor o el recurso no admite. Los tipos de medio admitidos son: application/json, application/xml",
                "E305",
                "Unsupported Media Type.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    // Este manejador captura cuando el JSON está malformado o tiene datos inválidos
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException e,
            HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.create(
                400,
                "La solicitud contiene un formato JSON inválido o datos malformados.",
                "E400",
                "Bad Request - Malformed JSON.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Este manejador captura errores de base de datos (tabla no existe, constraint violation, etc.)
    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(
            org.springframework.dao.DataAccessException e,
            HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.create(
                500,
                "El servidor encontró una condición inesperada que le impidió cumplir con la solicitud",
                "E307",
                "An unexpected error occurred on the server.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.create(
                500,
                "El servidor encontró una condición inesperada que le impidió cumplir con la solicitud",
                "E307",
                "An unexpected error occurred on the server.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}