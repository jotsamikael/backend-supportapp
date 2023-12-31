package com.example.backendsupportapp.filter;

import com.example.backendsupportapp.constant.SecurityConstant;
import com.example.backendsupportapp.utility.JWTTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/*Filter fires each time there is a request and is only fired once*/

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JWTTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       if(request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)){
           response.setStatus(HttpStatus.OK.value());
       } else {

           String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
           if(authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)){
               System.out.println("Auth header is null or doesn't start with bearer");

               filterChain.doFilter(request, response);
              return;
           }

           String token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
           System.out.println(token);
           String username = jwtTokenProvider.getSubject(token);
           System.out.println(username);
           System.out.println(jwtTokenProvider.isTokenValid(username, token));
           System.out.println(SecurityContextHolder.getContext().getAuthentication() == null);
           if(jwtTokenProvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null){
               List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);

               Authentication authentication =  jwtTokenProvider.getAuthentication(username, authorities, request);
               SecurityContextHolder.getContext().setAuthentication(authentication);
               System.out.println("condition1");

           } else{
               SecurityContextHolder.clearContext();
               System.out.println("condition2");

           }
       }
       filterChain.doFilter(request, response);
    }
}
