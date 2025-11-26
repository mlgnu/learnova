package org.mlgnu.learnova.module.auth.model;

import lombok.AllArgsConstructor;
import org.mlgnu.learnova.module.user.model.entity.UserAccount;
import org.mlgnu.learnova.module.user.model.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class AppUserDetails implements UserDetails {

    private final UserAccount user;

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(AppRole::new).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getStatus() != AccountStatus.DISABLED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != AccountStatus.BANNED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() != AccountStatus.DELETED;
    }
}
