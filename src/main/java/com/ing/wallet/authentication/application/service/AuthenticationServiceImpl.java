package com.ing.wallet.authentication.application.service;

import com.ing.wallet.authentication.application.dto.request.LoginRequest;
import com.ing.wallet.authentication.application.dto.response.UserDto;
import com.ing.wallet.customer.domain.entity.Customer;
import com.ing.wallet.authentication.domain.enums.AuthorityType;
import com.ing.wallet.customer.domain.repository.CustomerRepository;
import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.security.token.TokenService;
import com.ing.wallet.infrastructure.utils.AuthUtils;
import com.ing.wallet.infrastructure.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final TokenService tokenService;

    @Override
    public UserDto login(LoginRequest loginRequest) {
        log.info("Authentication login started. Email: {}", loginRequest.email());
        Customer customer = getCustomer(loginRequest);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.email(), loginRequest.password()
        ));

        UserDto userDto = UserDto.builder()
                .name(customer.getName())
                .surname(customer.getSurname())
                .email(customer.getEmail())
                .isEnabled(true)
                .authorities(Set.of(AuthorityType.LOGGED_IN, getAuthorityType(customer)))
                .build();
        tokenService.createToken(userDto);
        AuthUtils.authenticate(userDto);

        return userDto;
    }

    private AuthorityType getAuthorityType(Customer customer) {
        return AuthorityType.fromString(customer.getAuthorityType());
    }

    private Customer getCustomer(LoginRequest loginRequest) {
        String email = loginRequest.email();
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> LogUtils.logAndThrowError(ExceptionCodes.CUSTOMER_NOT_FOUND));
    }
}
