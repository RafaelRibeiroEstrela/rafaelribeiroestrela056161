package com.example.processoseletivoapi.configs;

import com.example.processoseletivoapi.exceptions.AccessLimitException;
import com.example.processoseletivoapi.exceptions.AuthorizationException;
import com.example.processoseletivoapi.exceptions.TokenException;
import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.models.enums.TokenTypeEnum;
import com.example.processoseletivoapi.services.RateLimitService;
import com.example.processoseletivoapi.services.RoleService;
import com.example.processoseletivoapi.services.TokenService;
import com.example.processoseletivoapi.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static com.example.processoseletivoapi.configs.SecurityConfig.PUBLIC_PATHS;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final UserService userService;
    private final RoleService roleService;
    private final RateLimitService rateLimitService;

    private final HandlerExceptionResolver resolver;


    private final RequestMatcher publicEndpoints =
            new OrRequestMatcher(
                    Arrays.stream(PUBLIC_PATHS)
                            .map(p -> PathPatternRequestMatcher.withDefaults().matcher(p))
                            .toArray(RequestMatcher[]::new)
            );

    @Value("${environment}")
    private String environment;

    public SecurityFilter(TokenService tokenService, UserService userService, RoleService roleService, RateLimitService rateLimitService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.roleService = roleService;
        this.rateLimitService = rateLimitService;
        this.resolver = resolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (environment.equals("dev")) {
            return true;
        }
        return publicEndpoints.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        try {

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                String token = recoverToken(request);

                try {
                    tokenService.validateToken(token, TokenTypeEnum.TOKEN);
                } catch (TokenException e) {
                    throw new AuthorizationException(e.getMessage());
                }

                String username = tokenService.extractSubject(token);

                User user = userService.findByUsername(username);

                rateLimitService.registerAccess(username);

                if (!rateLimitService.isAccessAllowed(username)) {
                    throw new AccessLimitException("Limite de acessos atingido");
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

        } catch (AccessLimitException | AuthorizationException e) {
            SecurityContextHolder.clearContext();
            resolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            resolver.resolveException(request, response, null, e);
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
