//package com.taskengine.taskengine_user_service.filter;
//
//import com.taskengine.taskengine_user_service.service.CustomUserDetailsService;
//import com.taskengine.taskengine_user_service.utils.JwtUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Autowired
//    private JwtUtils jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader=request.getHeader("Authorization");
//        String token=null;
//        String userName=null;
//        if(authHeader!=null && authHeader.contains("Bearer ")){
//            token=authHeader.substring(7);
//            userName=jwtUtil.extractUsername(token);
//        }
//
//        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
//            UserDetails userDetails= customUserDetailsService.loadUserByUsername(userName);
//            if(jwtUtil.validatToken(userName,token,userDetails)){
//                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request,response);
//    }
//}
