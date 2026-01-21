package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserService userService, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    public String login(String username, String password) {
        User user = userService.findByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("username or password incorrect");
        }
        return tokenService.generate(username);
    }

    public void logout(String token) {
        tokenService.delete(token);
        SecurityContextHolder.clearContext();
    }
}
