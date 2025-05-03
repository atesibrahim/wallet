package com.ing.wallet.infrastructure.exception;

import com.ing.wallet.authentication.application.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletBusinessException.class)
    public ResponseEntity<ApiResponse<String>> handleBusinessException(final WalletBusinessException e) {
        ApiResponse<String> apiResponse = ApiResponse.failure(e.getExceptionCode().toErrorResponse());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(final AuthenticationException e) {
        ApiResponse<String> apiResponse = ApiResponse.failure(ExceptionCodes.AUTHENTICATION_ERROR.toErrorResponse());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthorizationDeniedException(final AuthorizationDeniedException e) {
        ApiResponse<String> apiResponse = ApiResponse.failure(ExceptionCodes.AUTHORIZATION_ERROR.toErrorResponse());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(final AccessDeniedException e) {
        ApiResponse<String> apiResponse = ApiResponse.failure(ExceptionCodes.ACCESS_DENIED_ERROR.toErrorResponse());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(final BadCredentialsException e) {
        ApiResponse<String> apiResponse = ApiResponse.failure(ExceptionCodes.EMAIL_OR_PASSWORD_ERROR.toErrorResponse());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleBusinessException(final Exception e) {
        ApiResponse<String> apiResponse = ApiResponse.failure(ExceptionCodes.INTERNAL_SERVER_ERROR.toErrorResponse());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

}