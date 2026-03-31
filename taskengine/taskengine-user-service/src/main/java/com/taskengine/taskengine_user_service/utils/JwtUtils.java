package com.taskengine.taskengine_user_service.utils;

import com.taskengine.taskengine_user_service.configuration.RsaKeyConfig;
import com.taskengine.taskengine_user_service.model.Role;
import com.taskengine.taskengine_user_service.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtils {

    @Autowired
    private RsaKeyConfig rsaKeyPair;

    @Autowired
    private JwtTokenService jwtTokenService;


    //TODO: Generate User Token
    public  String generateUserToken(String email, Role role){

        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_"+role);

        role.getPermissions().forEach(permissions -> authorities.add(permissions.name()));

        return jwtTokenService.generateToken(
                Map.of(
                        "authorities",authorities,
                        "token_type","USER"
                ),
                email,
                18000
        );
    }
}
