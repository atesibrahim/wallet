package com.ing.wallet.infrastructure.config;

import com.ing.wallet.infrastructure.constant.AuthorizationHeaderWriter;
import com.ing.wallet.infrastructure.constant.SecurityConfigConstants;
import com.ing.wallet.authentication.domain.enums.AuthorityType;
import com.ing.wallet.infrastructure.security.AccessLogFilter;
import com.ing.wallet.infrastructure.security.CustomAccessDeniedHandler;
import com.ing.wallet.infrastructure.security.JwtUserDetailsConverter;
import com.ing.wallet.infrastructure.security.ValidatedScopeChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUserDetailsConverter jwtUserDetailsConverter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AccessLogFilter accessLogFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers(SecurityConfigConstants.PUBLIC_ENDPOINTS).permitAll();
                            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                            auth.requestMatchers(HttpMethod.POST, SecurityConfigConstants.LOGGED_IN_ENDPOINTS_POST);
                            auth.requestMatchers(HttpMethod.GET, SecurityConfigConstants.AUTH_BASE_PATH)
                                    .access(getCustomerLoggedInScope());
                            auth.requestMatchers(HttpMethod.GET, SecurityConfigConstants.CUSTOMER_ENDPOINTS_GET)
                                    .access(getCustomerLoggedInScope());
                            auth.requestMatchers(HttpMethod.GET, SecurityConfigConstants.WALLET_ENDPOINTS_GET)
                                    .access(getCustomerLoggedInScope());
                            auth.requestMatchers(HttpMethod.POST, SecurityConfigConstants.WALLET_ENDPOINTS_POST)
                                    .access(getCustomerLoggedInScope());
                            auth.requestMatchers(HttpMethod.GET, SecurityConfigConstants.TRANSACTION_ENDPOINTS_GET)
                                    .access(getCustomerLoggedInScope());
                            auth.requestMatchers(HttpMethod.POST, SecurityConfigConstants.TRANSACTION_ENDPOINTS_POST)
                                    .access(getCustomerLoggedInScope());
                            auth.requestMatchers(HttpMethod.POST,SecurityConfigConstants.TRANSACTION_ENDPOINTS_APPROVE_POST)
                                    .access(getAdminLoggedInScope());
                            auth.requestMatchers(HttpMethod.POST, SecurityConfigConstants.AUTH_BASE_PATH + "/logout")
                                    .authenticated();
                        })
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .oauth2ResourceServer(
                        httpSecurityOAuth2ResourceServerConfigurer ->
                                httpSecurityOAuth2ResourceServerConfigurer.jwt(
                                        jwtConfigurer ->
                                                jwtConfigurer.jwtAuthenticationConverter(jwtUserDetailsConverter)))
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer ->
                                httpSecurityExceptionHandlingConfigurer
                                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(accessLogFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static ValidatedScopeChecker getCustomerLoggedInScope() {
        return new ValidatedScopeChecker(
                AuthorityType.LOGGED_IN,
                AuthorityType.CUSTOMER);
    }

    private static ValidatedScopeChecker getAdminLoggedInScope() {
        return new ValidatedScopeChecker(
                AuthorityType.LOGGED_IN,
                AuthorityType.ADMIN);
    }
}
