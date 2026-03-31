package com.taskengine.taskengine_user_service.controller;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taskengine.taskengine_user_service.annotation.AuditLog;
import com.taskengine.taskengine_user_service.dto.AuthDTO;
import com.taskengine.taskengine_user_service.dto.OAuthClientDTO;
import com.taskengine.taskengine_user_service.dto.ResponseDTO;
import com.taskengine.taskengine_user_service.model.OAuthClient;
import com.taskengine.taskengine_user_service.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/auth/api/v1")
public class AuthController {

//    @Autowired
//    private JwtUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  JWKSource<SecurityContext> jwkSource;

    @Autowired
    private AuthService authService;

    @PostMapping("/login/token")
    @AuditLog
    public String generateToken(@RequestBody  AuthDTO authDTO){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDTO.username(),authDTO.password()
                    )
            );
           return authService.generateUserToken(authDTO.username(),authDTO.password());
        }



    @PostMapping(value = "/oauth/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @AuditLog
    public ResponseEntity<?> generateOauthToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam(value = "client_id",required = false) String clientId,
            @RequestParam(value = "client_secret",required = false) String clientSecret,
            @RequestHeader(value = "Authorization",required = false) String basicAuthHeader
    ){
       return authService.generateOauthClientToken(grantType,clientId,clientSecret,basicAuthHeader);
    }


//    // for client-authentication-method: client_secret_post:
//    public  ResponseEntity<Map<String,Object>> generateOauthToken(@RequestParam("grant_type") String grantType,
//                                                  @RequestParam("client_id") String clientId,
//                                                  @RequestParam("client_secret") String clientSecret){
//        if(!"client_credentials".equals(grantType)){
//            return ResponseEntity.badRequest().body(Map.of("error","unsupported_grant_type"));
//        }
//
//        String token=authService.generateOAuthToken(clientId,clientSecret);//          System.out.println("For client-authentication-method: client_secret_basic");
//
//
//           logger.info("From client-authentication-method: client_secret_Post");
//        return ResponseEntity.ok(Map.of(
//                "access_token", token,
//                "token_type", "Bearer",
//                "expires_in", 3600
//
//        ));
//    }


    @PostMapping("/client/register")
    public ResponseEntity<ResponseDTO<OAuthClientDTO>> registerClient(@RequestBody OAuthClientDTO oAuthClientDTO){
        return authService.registerClient(oAuthClientDTO);
    }


    @GetMapping("/.well-known/jwks.json")
    public Map<String,Object> key() throws Exception {
        JWKSet jwkSet= new JWKSet(jwkSource.get(new JWKSelector(new JWKMatcher.Builder().build()),null));
        return jwkSet.toJSONObject();
    }
}
