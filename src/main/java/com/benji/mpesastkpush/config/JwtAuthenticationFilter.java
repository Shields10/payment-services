package com.benji.mpesastkpush.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull
    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName; // User name is contact


        if (authHeader==null || !authHeader.startsWith(("Bearer "))){
            filterChain.doFilter(request,response);
        return;
        }
        // Extract token from auth header
        jwt = authHeader.substring(7); // The token will be after the bearer
        userName =jwtService.extractUserName(jwt);  //  extract the user contact from JWT token;
         if (userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
             UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

             if (jwtService.isTokenValid(jwt,userDetails)){
                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                         null,
                         userDetails.getAuthorities());
                 authToken.setDetails(
                         new WebAuthenticationDetailsSource()
                                 .buildDetails(request)
                 );
                 // Update Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
             }
            filterChain.doFilter(request,response);
         }

    }
}
