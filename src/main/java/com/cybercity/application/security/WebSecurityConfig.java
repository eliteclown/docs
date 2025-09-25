package com.cybercity.application.security;

import com.cybercity.application.entities.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrfConfig-> csrfConfig.disable())
                .sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**/**","/course/**/**","/webinar/**/**","/api/webinar-enrollments/webinar/{webinarId}","/api/webinar-enrollments/{id}").hasRole(String.valueOf(Role.ADMIN))
                        .requestMatchers("/enroll/get/{userId}","/user/update/{userId}","/api/webinar-enrollments/enroll","/api/webinar-enrollments/{id}","/api/webinar-enrollments/user/{userId}").hasRole(String.valueOf(Role.STUDENT))
                        .requestMatchers("/bookings/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exHandlingConfig -> exHandlingConfig.accessDeniedHandler(accessDeniedHandler()));
        return httpSecurity.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, accessDeniedException) -> {
            handlerExceptionResolver.resolveException(request,response,null,accessDeniedException);
        };
    }
}

