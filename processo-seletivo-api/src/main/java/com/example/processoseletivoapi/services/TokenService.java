package com.example.processoseletivoapi.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.processoseletivoapi.dtos.TokenDTO;
import com.example.processoseletivoapi.exceptions.TokenException;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.models.Token;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.models.enums.TokenTypeEnum;
import com.example.processoseletivoapi.repositories.TokenRepository;
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

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final RoleService roleService;

    public TokenService(TokenRepository tokenRepository, UserService userService, RoleService roleService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    public String generate(String subject, TokenTypeEnum tokenType) {
            if (tokenType.equals(TokenTypeEnum.TOKEN_PRE_ASSINADO)) {
                return generate(TokenTypeEnum.TOKEN_PRE_ASSINADO, subject);
            }
            User user = userService.findByUsername(subject);
            List<String> roles = roleService.findAllByIds(user.getRolesInHashSet()).stream()
                    .map(Role::getAuthority)
                    .toList();
           return generate(tokenType, subject, roles);
    }

    public void validateToken(String token, TokenTypeEnum tokenType) {
        if (token == null || token.trim().isEmpty()) {
            throw new TokenException("Erro de token: O token está vazio");
        }
        if (tokenRepository.findById(token).isPresent()) {
            throw new TokenException("Erro de token: O token foi inutilizado");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
        } catch (Exception e) {
            throw new TokenException("Erro de token: " + e.getMessage());
        }
        if (!TokenTypeEnum.valueOf(JWT.decode(token).getClaims().get("tokenType").asString()).equals(tokenType)) {
            throw new TokenException("Erro de token: O tipo de token utilizado é inválido");
        }
    }

    public TokenDTO refreshToken(String refreshToken) {
        validateToken(refreshToken, TokenTypeEnum.REFRESH_TOKEN);
        delete(refreshToken);
        String subject = extractSubject(refreshToken);
        String token = generate(subject, TokenTypeEnum.TOKEN);
        refreshToken = generate(subject, TokenTypeEnum.REFRESH_TOKEN);
        return new TokenDTO(token, refreshToken);
    }

    public void delete(String token) {
        tokenRepository.save(new Token(token, LocalDateTime.now()));
    }

    public String extractSubject(String bearerToken) {
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

    private String generate(TokenTypeEnum tokenType, String subject, List<String> roles) {
        try {
            Instant now = Instant.now();
            Instant exp = now.plusSeconds(tokenType.getExpiration());
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withSubject(subject)
                    .withIssuer(issuer)
                    .withClaim("tokenType", tokenType.name())
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(exp))
                    .withArrayClaim("roles", roles.toArray(new String[0]))
                    .sign(algorithm);
        } catch (JWTVerificationException e) {
            throw new TokenException(e.getMessage());
        }
    }

    private String generate(TokenTypeEnum tokenType, String subject) {
        try {
            Instant now = Instant.now();
            Instant exp = now.plusSeconds(tokenType.getExpiration());
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withSubject(subject)
                    .withIssuer(issuer)
                    .withClaim("tokenType", tokenType.name())
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(exp))
                    .sign(algorithm);
        } catch (JWTVerificationException e) {
            throw new TokenException(e.getMessage());
        }
    }
}
