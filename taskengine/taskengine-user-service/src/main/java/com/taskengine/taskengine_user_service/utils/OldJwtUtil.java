//package com.taskengine.taskengine_user_service.utils;
//
//import com.taskengine.taskengine_user_service.configuration.RsaKeyConfig;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    private final long EXPIRATION_TIME=1000*60*30;
//    private final String SECRET="abcdefghijklmopqrstuvwxyz1234567890AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//    private final SecretKey key= Keys.hmacShaKeyFor(SECRET.getBytes());
//
//    @Autowired
//    private RsaKeyConfig rsaKeyConfig;
//
//    //TODO: JwtTokenService
//
//    public String generateToken(String email){
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setIssuer("Auth-Service")
//                .signWith(key, SignatureAlgorithm.HS256)
//                .setExpiration(new Date(System.currentTimeMillis() +EXPIRATION_TIME))
//                .compact();
//    }
//
//
//    public  String extractUsername(String token) {
//            return extractClaims(token).getSubject();
//    }
//
//    private Claims extractClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .requireIssuer("Auth-user")
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public boolean validatToken(String userName, String token, UserDetails userDetails) {
//        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractClaims(token).getExpiration().before(new Date());
//    }
//}
