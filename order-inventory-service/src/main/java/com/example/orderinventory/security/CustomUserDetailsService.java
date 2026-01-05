package com.example.orderinventory.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Simple adapter that delegates to the configured UserDetailsService bean.
 * This keeps JwtFilter working without introducing a second user store.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsService delegate;

    @Autowired
    public CustomUserDetailsService(UserDetailsService delegate) {
        this.delegate = delegate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return delegate.loadUserByUsername(username);
    }
}