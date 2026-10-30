package com.example.messengerapplication.filter;

import com.example.messengerapplication.repository.UserRepository;
import com.example.messengerapplication.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try{
//            if(token==null || !token.startsWith("Bearer ")) {
//                filterChain.doFilter(request, response);
//                return;
//            }
            String username=jwtService.extractUsername(token);

            if(SecurityContextHolder.
                    getContext().
                    getAuthentication()==null &&
                    userRepository.findByUsernameIgnoreCase(username)
                            !=null){

                    UsernamePasswordAuthenticationToken authentication = new
                            UsernamePasswordAuthenticationToken(username,
                            null, Collections.emptyList());

                    authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                    );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
