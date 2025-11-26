package org.mlgnu.learnova.module.auth.service;

import lombok.AllArgsConstructor;
import org.mlgnu.learnova.module.auth.dto.LoginRequest;
import org.mlgnu.learnova.module.auth.dto.SignupRequest;
import org.mlgnu.learnova.module.user.model.entity.UserAccount;
import org.mlgnu.learnova.module.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void signup(SignupRequest request) {

        userRepository.uniqueUserAccountByEmailOrThrow(request.email());
        userRepository.uniqueUserAccountByUsernameOrThrow(request.username());

        UserAccount user = new UserAccount();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setDisplayName(request.name());

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                (new UsernamePasswordAuthenticationToken(request.username(), request.password())));
        return jwtService.generateToken(authentication);
    }
}
