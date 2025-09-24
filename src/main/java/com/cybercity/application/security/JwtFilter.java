package com.cybercity.application.security;

import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.services.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public final JwtService jwtService;
    public final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            log.info("JWT Filter triggered for path: " + request.getRequestURI());
            final String requestTokenHeader = request.getHeader("Authorization");
            if(requestTokenHeader==null|| !requestTokenHeader.startsWith("Bearer")){
                filterChain.doFilter(request,response);
                return;
            }
            String token = requestTokenHeader.substring(7).trim();
            Long userId = jwtService.getUserIdByToken(token);

            if(userId!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserEntity user = userService.getUserByIdAuth(userId);
                //  check if the user should be allowed
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
            filterChain.doFilter(request,response);
        }catch (JwtException e){
            handlerExceptionResolver.resolveException(request,response,null,e);
        }
    }
}
