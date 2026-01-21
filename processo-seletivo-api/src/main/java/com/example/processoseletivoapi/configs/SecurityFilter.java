package com.example.processoseletivoapi.configs;

import com.example.processoseletivoapi.exceptions.AuthorizationException;
import com.example.processoseletivoapi.models.RateLimit;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.services.RateLimitService;
import com.example.processoseletivoapi.services.RoleService;
import com.example.processoseletivoapi.services.TokenService;
import com.example.processoseletivoapi.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final UserService userService;
    private final RoleService roleService;
    private final RateLimitService rateLimitService;

    @Value("${environment}")
    private String environment;

    public SecurityFilter(TokenService tokenService, UserService userService, RoleService roleService, RateLimitService rateLimitService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.roleService = roleService;
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (environment.equals("dev")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                String token = recoverToken(request);

                if (token == null) {
                    throw new AuthorizationException("Invalid token");
                }

                if (!tokenService.isValidToken(token)) {
                    throw new AuthorizationException("Invalid token");
                }

                String username = tokenService.extractUsername(token);

                User user = userService.findByUsername(username);

                rateLimitService.registrarAcesso(username);

                if (!rateLimitService.verificarSeAcessoPermitido(username)) {
                    LOGGER.error("Limite de acessos atingido");
                    throw new AuthorizationException("Limite de acessos atingido");
                }

                Collection<? extends GrantedAuthority> roles = buildGrantedAuthority(roleService.findAllByIds(user.getRolesInHashSet()));

                UserDetails userDetails = buildUserDetails(user, roles);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        roles
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) return null;

        header = header.trim();
        if (!header.startsWith(BEARER_PREFIX)) return null;

        String token = header.substring(BEARER_PREFIX.length()).trim();
        return token.isEmpty() ? null : token;
    }

    private UserDetails buildUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorities;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    private Collection<? extends GrantedAuthority> buildGrantedAuthority(Set<Role> roles) {
        return roles.stream().map(role -> (GrantedAuthority) role::getAuthority).toList();
    }
}
