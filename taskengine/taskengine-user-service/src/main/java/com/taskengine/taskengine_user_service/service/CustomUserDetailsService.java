package com.taskengine.taskengine_user_service.service;

import com.taskengine.taskengine_user_service.exception.UserNotFoundException;
import com.taskengine.taskengine_user_service.model.CustomUserDetails;
import com.taskengine.taskengine_user_service.model.User;
import com.taskengine.taskengine_user_service.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user= userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found with this Email::"+email));
        return new CustomUserDetails(user);
    }
}
