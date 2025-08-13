package com.myteam.household_book.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/** JWT에서 꺼낸 userId/username을 보관하는 Principal */
public class CustomUserPrincipal implements UserDetails {
    private final Long id;
    private final String username;

    public CustomUserPrincipal(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() { return id; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
    @Override public String getPassword() { return ""; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
