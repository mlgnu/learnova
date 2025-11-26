package org.mlgnu.learnova.module.auth.model;

import lombok.AllArgsConstructor;
import org.mlgnu.learnova.module.user.model.entity.Role;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class AppRole implements GrantedAuthority {

    private final Role role;

    @Override
    public String getAuthority() {
        return role.getName();
    }
}
