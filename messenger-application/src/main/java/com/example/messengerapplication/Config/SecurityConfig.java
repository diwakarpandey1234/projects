package com.example.messengerapplication.Config;


import com.example.messengerapplication.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> filterRegistrationBean(JwtAuthenticationFilter filter) {

        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);

        registration.setEnabled(false);
        return registration;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http.
                  csrf(csrf -> csrf.disable()).
                  sessionManagement(session->
                          session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                  authorizeHttpRequests(auth->

                          auth.requestMatchers(
                                  HttpMethod.POST,"/api/v1/auth/register",
                                  "/api/v1/auth/login").permitAll().
                                  anyRequest().authenticated()
                          ).formLogin(form->form.disable())
                  .httpBasic(form->form.disable());

        return http.build();
    }
}
