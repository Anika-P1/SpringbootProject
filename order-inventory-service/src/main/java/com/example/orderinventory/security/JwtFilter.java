package com.example.orderinventory.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired(required = false)
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // If JWT support not wired, just continue (we use BasicAuth by default)
        if (jwtUtil == null || userDetailsService == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        String usernameStr = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                Object extracted = jwtUtil.extractUsername(jwt); // defensive: might return String or UserDetails
                if (extracted instanceof String) {
                    usernameStr = (String) extracted;
                } else if (extracted instanceof UserDetails) {
                    usernameStr = ((UserDetails) extracted).getUsername();
                } else if (extracted != null) {
                    usernameStr = extracted.toString();
                }
            } catch (ExpiredJwtException e) {
                log.warn("JWT token expired: {}", e.getMessage());
            } catch (JwtException e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
            } catch (RuntimeException e) {
                log.warn("Failed to extract username from JWT: {}", e.getMessage());
            }
        }

        if (usernameStr != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(usernameStr);
            boolean valid = validateTokenDynamic(jwt, usernameStr, userDetails);
            if (userDetails != null && valid) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Defensive validator: try to invoke validateToken(jwt, UserDetails) or validateToken(jwt, String)
     * using reflection so we don't bind to a single compile-time signature.
     */
    private boolean validateTokenDynamic(String jwt, String username, UserDetails userDetails) {
        if (jwt == null) return false;
        Class<?> cls = jwtUtil.getClass();

        // Try (String, UserDetails)
        try {
            Method m = cls.getMethod("validateToken", String.class, UserDetails.class);
            Object result = m.invoke(jwtUtil, jwt, userDetails);
            return Boolean.TRUE.equals(result);
        } catch (NoSuchMethodException ignored) {
            // fallthrough
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.debug("validateToken(String,UserDetails) invocation failed: {}", e.getMessage());
        }

        // Try (String, String)
        try {
            Method m2 = cls.getMethod("validateToken", String.class, String.class);
            Object result = m2.invoke(jwtUtil, jwt, username);
            return Boolean.TRUE.equals(result);
        } catch (NoSuchMethodException ignored) {
            // fallthrough
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.debug("validateToken(String,String) invocation failed: {}", e.getMessage());
        }

        // Last-resort: try a single-arg validateToken(String)
        try {
            Method m3 = cls.getMethod("validateToken", String.class);
            Object result = m3.invoke(jwtUtil, jwt);
            return Boolean.TRUE.equals(result);
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.debug("validateToken(String) invocation failed: {}", e.getMessage());
        }

        log.warn("No compatible validateToken(...) method found on JwtUtil - rejecting token");
        return false;
    }
}