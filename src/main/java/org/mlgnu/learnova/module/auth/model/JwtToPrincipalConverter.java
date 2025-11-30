package org.mlgnu.learnova.module.auth.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtToPrincipalConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Long id = Long.valueOf(jwt.getSubject());
        List<SimpleGrantedAuthority> authorities = jwt.getClaimAsStringList("scope").stream()
                .map(authority -> new SimpleGrantedAuthority("SCOPE_" + authority))
                .toList();
        String username = jwt.getClaimAsString("username");

        JwtUserPrincipal principal = new JwtUserPrincipal(id, username, authorities);
        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }
}
