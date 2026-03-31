package com.taskengine.taskengine_user_service.utils;

import com.taskengine.taskengine_user_service.configuration.RsaKeyConfig;
import com.taskengine.taskengine_user_service.service.JwtTokenService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClientTokenUtils {


    //TODO: Insert the keyPair
    @Autowired
    private RsaKeyConfig rsaKeyPair;

    @Autowired
    private JwtTokenService jwtTokenService;


    //TODO: Create method to generate token
    public String generateClientToken(@NotNull String clientId) {
        //TODO:  return Jwts.builder()
        return jwtTokenService.generateToken(
                Map.of(
                        "scope","internal",
                        "token_type","CLIENT"
                ),
                clientId,
                900
        );



    }


}
