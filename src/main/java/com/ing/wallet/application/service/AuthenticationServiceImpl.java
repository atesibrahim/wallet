package com.ing.wallet.application.service;

import com.ing.wallet.application.dto.request.LoginRequest;
import com.ing.wallet.application.dto.response.ApiResponse;
import com.ing.wallet.application.dto.response.UserDto;
import com.ing.wallet.domain.entity.Customer;
import com.ing.wallet.domain.enums.AuthorityType;
import com.ing.wallet.domain.repository.CustomerRepository;
import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.security.token.TokenService;
import com.ing.wallet.infrastructure.utils.AuthUtils;
import com.ing.wallet.infrastructure.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.ing.wallet.infrastructure.constant.MessageConstants.LOGGED_IN_SUCCESSFULLY;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto login(LoginRequest loginRequest) {
        Customer customer = getCustomer(loginRequest);
        String password = passwordEncoder.encode(loginRequest.password());

        User user = buildUserWithCustomer(customer);

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

    private static AuthorityType getAuthorityType(Customer customer) {
        return AuthorityType.fromString(customer.getAuthorityType());
    }

    private User buildUserWithCustomer(Customer customer) {
        return new User(
                customer.getEmail(),
                customer.getPassword(),
                true,
                true,
                true,
                true,
                Set.of(AuthorityType.LOGGED_IN, getAuthorityType(customer))
        );
    }

    private Customer getCustomer(LoginRequest loginRequest) {
        String email = loginRequest.email();
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> LogUtils.logAndThrowError(ExceptionCodes.CUSTOMER_NOT_FOUND));
    }
}
