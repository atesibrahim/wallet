package com.ing.wallet.authentication.adapter;

import com.ing.wallet.authentication.application.dto.request.LoginRequest;
import com.ing.wallet.authentication.application.dto.response.ApiResponse;
import com.ing.wallet.authentication.application.dto.response.UserDto;
import com.ing.wallet.authentication.application.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<UserDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("authentication login started");
        return ApiResponse.success(authenticationService.login(loginRequest));
    }
}
