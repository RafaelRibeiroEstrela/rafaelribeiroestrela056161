package com.example.processoseletivoapi.services;

import com.example.processoseletivoapi.dtos.TokenDTO;
import com.example.processoseletivoapi.exceptions.BusinessException;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.models.enums.TokenTypeEnum;
import com.example.processoseletivoapi.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDTO login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException("username or password incorrect"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("username or password incorrect");
        }
        String token = tokenService.generate(username, TokenTypeEnum.TOKEN);
        String refreshToken = tokenService.generate(username, TokenTypeEnum.REFRESH_TOKEN);
        return new TokenDTO(token, refreshToken);
    }

    public void logout(String token, String refreshToken) {
        tokenService.delete(token);
        tokenService.delete(refreshToken);
        SecurityContextHolder.clearContext();
    }
}
