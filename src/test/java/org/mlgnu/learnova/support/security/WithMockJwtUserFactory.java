package org.mlgnu.learnova.support.security;

import org.mlgnu.learnova.module.auth.model.JwtUserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithMockJwtUserFactory implements WithSecurityContextFactory<WithMockJwtUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JwtUserPrincipal principal = new JwtUserPrincipal(
                annotation.id(),
                annotation.username(),
                Arrays.stream(annotation.authorities())
                        .map(SimpleGrantedAuthority::new)
                        .toList());

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }
}
