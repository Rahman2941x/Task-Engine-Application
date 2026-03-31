package com.taskengine.taskengine_user_service.configuration;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taskengine.taskengine_user_service.utils.PemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class RsaKeyConfig {


    @Value("${security.jwt.public-key}")
    private Resource publicKey;

    @Value("${security.jwt.private-key}")
    private Resource privateKey;

    @Bean
    public KeyPair keyPair() throws Exception{
        //create environment variable and store the private and public key use it in AuthService for OAuthToken Generation
        return new KeyPair(
                PemUtils.readPublicKey(publicKey),
                PemUtils.readPrivateKey(privateKey)
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(KeyPair keyPair){

        JWK jwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID("task-engine-key")
                .build();

        JWKSource<SecurityContext> jwkSource=new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair){
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair){

        RSAKey key=new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((PrivateKey) keyPair.getPrivate())
                .keyID("task-engine-key")
                .build();

        JWKSet jwkSet=new JWKSet(key);

        return (JWKSelector,SecurityContext)->JWKSelector.select(jwkSet);
    }

}
