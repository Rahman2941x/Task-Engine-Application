package com.taskengine.taskengine_user_service.service;

import com.taskengine.taskengine_user_service.dto.OAuthClientDTO;
import com.taskengine.taskengine_user_service.dto.ResponseDTO;
import com.taskengine.taskengine_user_service.exception.UserAlreadyExist;
import com.taskengine.taskengine_user_service.exception.UserNotFoundException;
import com.taskengine.taskengine_user_service.model.OAuthClient;
import com.taskengine.taskengine_user_service.model.User;
import com.taskengine.taskengine_user_service.repository.OAuthClientRepo;
import com.taskengine.taskengine_user_service.repository.UserRepo;
import com.taskengine.taskengine_user_service.utils.ClientTokenUtils;
import com.taskengine.taskengine_user_service.utils.JwtUtils;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;


@Service
public class AuthService {

    Logger logger =LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private OAuthClientRepo oAuthClientRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientTokenUtils clientTokenUtil;

    @Autowired
    private JwtUtils jwtUtils;


    public String generateOAuthToken(@NotNull String clientId, @NotNull String clientSecret) {
        //validate Client IT
        OAuthClient oAuthClient=oAuthClientRepo.findByClientId(clientId)
                .orElseThrow(()->{
                    logger.warn("Unknown Client ID: {}",clientId);
                    return new RuntimeException("Unknown Client:: " + clientId);
                });

        //validate Secret
        if(!passwordEncoder.matches(clientSecret,oAuthClient.getClientSecret())){
            logger.warn("Invalid Client secret...");
            throw  new RuntimeException("Invalid Client Secret::");
        }

        //Route to Token Generation @Component (ClientTokenUtil)
        return  clientTokenUtil.generateClientToken(clientId);

    }

    public String generateUserToken(@NotNull String email, @NotNull String password) {
        User user=userRepo.findByEmail(email).orElseThrow(()->{
            logger.warn("Unknown User email: {}",email);
            return new UserNotFoundException("user Email is not Found Email: " + email);
        });

        if(!user.getActive()){
            logger.warn("User is not Active:: {}",email);
            throw  new RuntimeException("user is not Active::: "+email);
        }

        return jwtUtils.generateUserToken(user.getEmail(),user.getRole());
    }

    public ResponseEntity<ResponseDTO<OAuthClientDTO>> registerClient(OAuthClientDTO oAuthClientDTO) {

        if(oAuthClientRepo.existsByClientId(oAuthClientDTO.clientId())){
            logger.warn("Client Id Already register::{}",oAuthClientDTO.clientId());
            throw  new UserAlreadyExist("Client Id Already register::"+oAuthClientDTO.clientId());
        }

        OAuthClient client=new OAuthClient();
        client.setClientId(oAuthClientDTO.clientId());
        client.setClientSecret(passwordEncoder.encode(oAuthClientDTO.clientSecret()));

        oAuthClientRepo.save(client);

        return ResponseEntity.ok(new ResponseDTO<>(new OAuthClientDTO(client.getClientId(),null)));
    }

    public ResponseEntity<?> generateOauthClientToken(String grantType, String clientId, String clientSecret, String basicAuthHeader) {

        if (!"client_credentials".equals(grantType)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "unsupported_grant_type"));
        }

        // 🔹 If client_secret_basic
        if (basicAuthHeader != null && basicAuthHeader.startsWith("Basic ")) {
            String base64 = basicAuthHeader.substring(6);
            String decoded = new String(Base64.getDecoder().decode(base64));

            String[] parts = decoded.split(":");


            if(parts.length !=2){
                return ResponseEntity.badRequest().body(Map.of("Error","Invalid Client"));
            }
            clientId = parts[0];
            clientSecret = parts[1];
            logger.info("From client-authentication-method: client_secret_basic");
        }
        // 🔹 If client_secret_post
        else if (clientId!=null && clientSecret!=null){
            logger.info("From client-authentication-method: client_secret_post");
        }else {
            return ResponseEntity.badRequest().body(Map.of("Error","Invalid Client"));
        }

        String token = generateOAuthToken(clientId,clientSecret);

        return ResponseEntity.ok(
                Map.of(
                        "access_token", token,
                        "token_type", "Bearer",
                        "expires_in", 3600
                )
        );
    }
}
