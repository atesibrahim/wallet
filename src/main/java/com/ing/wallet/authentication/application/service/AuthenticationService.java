package com.ing.wallet.authentication.application.service;

import com.ing.wallet.authentication.application.dto.request.LoginRequest;
import com.ing.wallet.authentication.application.dto.response.UserDto;

public interface AuthenticationService {
    UserDto login(LoginRequest loginRequest);
}
