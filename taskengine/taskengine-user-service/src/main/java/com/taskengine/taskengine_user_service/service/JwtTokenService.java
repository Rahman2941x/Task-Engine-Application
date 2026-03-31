package com.taskengine.taskengine_user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class JwtTokenService {

    @Autowired
    public JwtEncoder jwtEncoder;


    public String generateToken(Map<String,Object> claims,
                                String subject,
                                long expirySeconds){

        Instant now=Instant.now();

        JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirySeconds))
                .subject(subject)
                .claims(c->c.putAll(claims))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}
