package com.taskengine.taskengine_user_service.configuration;

import com.taskengine.taskengine_user_service.model.Permissions;
import com.taskengine.taskengine_user_service.model.Role;
import com.taskengine.taskengine_user_service.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


//    @Autowired
//    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/user/api/v1/user/register",
                                        "/auth/api/v1/login/token",
                                        "/auth/api/v1/oauth/token",
                                        "/auth/api/v1/client/register",
                                "/auth/api/v1/.well-known/jwks.json"
                                ).permitAll()
                                .requestMatchers(HttpMethod.DELETE,"/user/**").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PATCH,"/user/api/v1/user/*/activation").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT,"/user/**").hasAuthority(Permissions.USER_WRITE.name())
                                .requestMatchers(HttpMethod.PATCH,"/user/**").hasAuthority(Permissions.USER_WRITE.name())
                                .anyRequest().authenticated())
                //.httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(auth->auth.jwt(jwt->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
    return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService){
        return customUserDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authProvider= new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){

        JwtGrantedAuthoritiesConverter converter= new JwtGrantedAuthoritiesConverter();

        converter.setAuthoritiesClaimName("authorities");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtConverter= new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtConverter;
    }

}
