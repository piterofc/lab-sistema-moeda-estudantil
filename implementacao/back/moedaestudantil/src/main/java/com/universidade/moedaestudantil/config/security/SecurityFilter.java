package com.universidade.moedaestudantil.config.security;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.universidade.moedaestudantil.model.Usuario;
import com.universidade.moedaestudantil.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    // CACHE SIMPLES: email -> CachedUser
    private static final long USER_CACHE_TTL_MS = TimeUnit.SECONDS.toMillis(60);
    private final ConcurrentHashMap<String, CachedUser> userCache = new ConcurrentHashMap<>();

    private static class CachedUser {
        final User user;
        final long createdAt;
        CachedUser(User u) {
            this.user = u;
            this.createdAt = System.currentTimeMillis();
        }
        boolean expired() {
            return System.currentTimeMillis() - createdAt > USER_CACHE_TTL_MS;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        if(login != null){
            /*
            User user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User Not Found"));
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            */
            try {
                User user = getCachedUser(login);
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException e) {
                // Log e continue (não bloquear a requisição por falha no DB)
                logger.error("Erro ao carregar usuário no filtro de segurança", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private User getCachedUser(String email) {
        CachedUser cu = userCache.get(email);
        if (cu != null && !cu.expired()) {
            return cu.user;
        }
        var user = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        userCache.put(email, new CachedUser(user));
        return user;
    }
}
