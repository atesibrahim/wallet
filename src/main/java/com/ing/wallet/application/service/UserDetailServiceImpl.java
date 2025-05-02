package com.ing.wallet.application.service;


import com.ing.wallet.domain.entity.Customer;
import com.ing.wallet.domain.repository.CustomerRepository;
import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.exception.WalletBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(()-> {
                    log.error("{} -- email/username : {} ", ExceptionCodes.EMAIL_NOT_FOUND_ERROR.getMessage(), email);
                    return new WalletBusinessException(ExceptionCodes.EMAIL_NOT_FOUND_ERROR);
                });

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of()
        );
    }
}
