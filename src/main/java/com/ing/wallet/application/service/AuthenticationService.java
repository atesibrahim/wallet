package com.ing.wallet.application.service;

import com.ing.wallet.application.dto.request.LoginRequest;
import com.ing.wallet.application.dto.response.ApiResponse;
import com.ing.wallet.application.dto.response.UserDto;

public interface AuthenticationService {
    UserDto login(LoginRequest loginRequest);
}
