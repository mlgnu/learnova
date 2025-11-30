package org.mlgnu.learnova.module.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class JwtUserPrincipal {
    private final Long id;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;
}
