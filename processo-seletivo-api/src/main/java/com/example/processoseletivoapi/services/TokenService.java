package com.example.processoseletivoapi.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.processoseletivoapi.exceptions.TokenException;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.models.Token;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.repositories.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class TokenService {

    @Value("${security.secret-key}")
    private String secretKey;
    @Value("${security.issuer}")
    private String issuer;
    private static final int EXPIRATION_TIME = 300;

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final RoleService roleService;

    public TokenService(TokenRepository tokenRepository, UserService userService, RoleService roleService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    public String generate(String username) {
        try {
            User user = userService.findByUsername(username);
            List<String> roles = roleService.findAllByIds(user.getRolesInHashSet()).stream()
                    .map(Role::getAuthority)
                    .toList();
            Instant now = Instant.now();
            Instant exp = now.plusSeconds(EXPIRATION_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withSubject(username)
                    .withIssuer(issuer)
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(exp))
                    .withArrayClaim("roles", roles.toArray(new String[0]))
                    .sign(algorithm);
        } catch (JWTVerificationException e) {
            throw new TokenException(e.getMessage());
        }
    }

    public boolean isValidToken(String token) {
        if (tokenRepository.findById(token).isPresent()) {
            return false;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public void delete(String token) {
        tokenRepository.save(new Token(token, LocalDateTime.now()));
    }

    public List<String> extractRoles(String bearerToken) {
        String token = cleanBearer(bearerToken);
        DecodedJWT jwt = JWT.decode(token);
        List<String> roles = jwt.getClaim("roles").asList(String.class);
        return roles == null ? Collections.emptyList() : roles;
    }

    public String extractUsername(String bearerToken) {
        String token = cleanBearer(bearerToken);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    private String cleanBearer(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank()) return null;
        String v = bearerToken.trim();
        if (v.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return v.substring(7).trim();
        }
        return v;
    }
}
