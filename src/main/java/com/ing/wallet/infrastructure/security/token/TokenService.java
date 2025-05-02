package com.ing.wallet.infrastructure.security.token;

import com.ing.wallet.application.dto.response.UserDto;

public interface TokenService {

  String createToken(UserDto user);

  void invalidateTokenByUser(UserDto user);

  UserDto getUser(Object source);
}
