package ru.market.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, "Missing or invalid Authorization header", 401);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenType = claims.get("type", String.class);
            if (!"access".equals(tokenType)) {
                sendError(response, "Not an access token", 401);
                return;
            }

            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            Object userIdObj = claims.get("user_id");
            Long userId = (userIdObj instanceof Integer) ? 
                         ((Integer) userIdObj).longValue() : 
                         (Long) userIdObj;

            List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
            );

            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    username, 
                    null, 
                    authorities
                );
            
            authentication.setDetails(new UserDetails(userId, username, role));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            sendError(response, "Invalid token", 401);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message, int status) 
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(
            String.format("{\"error\": \"%s\", \"message\": \"%s\"}", 
                status == 401 ? "Unauthorized" : "Forbidden", 
                message)
        );
    }

    public static class UserDetails {
        private final Long userId;
        private final String username;
        private final String role;

        public UserDetails(Long userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }

        public Long getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
    }
}