package com.authservice.dto;

import com.authservice.model.UserAuthority;
import lombok.Builder;

import java.util.Set;


@Builder
public record UserSecurityDetailGetDTO(
        Long ID,
        String email,
        boolean isAccountNonExpired,
        boolean isAccountNonLocked,
        boolean isCredentialsNonExpired,
        boolean isEnabled,

        Set<UserAuthority> auths
)
{
}
