package org.mlgnu.learnova.module.auth.service;

import lombok.AllArgsConstructor;
import org.mlgnu.learnova.module.auth.model.AppUserDetails;
import org.mlgnu.learnova.module.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findUserAccountByUsernameOrThrow(username);
        return new AppUserDetails(user);
    }
}
