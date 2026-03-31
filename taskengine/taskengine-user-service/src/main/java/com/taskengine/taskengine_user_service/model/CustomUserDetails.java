package com.taskengine.taskengine_user_service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private User user;
    private Role role;
    @Autowired
    private Permissions permissions;

    public CustomUserDetails(User user) {
        this.user=user;
        this.role=user.getRole();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities= new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
        Set<SimpleGrantedAuthority> authPermission=role.getPermissions().stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.name()))
                .collect(Collectors.toSet());
        authorities.addAll(authPermission);

        return authorities;
    }

    @Override
    public String getPassword() {return user.getPassword();}

    @Override
    public String getUsername() {return user.getEmail();}

    @Override
    public boolean isEnabled() {return user.getActive();}
}
