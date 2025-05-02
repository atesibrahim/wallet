package com.ing.wallet.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ing.wallet.domain.entity.Customer;
import com.ing.wallet.domain.enums.AuthorityType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Set;

@Getter
@Builder(toBuilder = true)
public final class UserDto implements UserDetails {
    @JsonProperty("enabled")
    private final boolean isEnabled;

    private final String username;
    private final String password;
    private final String name;
    private final String surname;
    private final String email;
    private final Set<AuthorityType> authorities;
    private final String providerToken;
    private final String providerRefreshToken;

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return isEnabled == userDto.isEnabled && Objects.equals(username, userDto.username) && Objects.equals(password, userDto.password) && Objects.equals(name, userDto.name) && Objects.equals(surname, userDto.surname)
                && Objects.equals(email, userDto.email) && Objects.equals(authorities, userDto.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isEnabled, username, password, name, surname, email, authorities);
    }
}