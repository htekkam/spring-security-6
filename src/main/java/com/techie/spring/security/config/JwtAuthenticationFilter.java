package com.techie.spring.security.config;

import com.techie.spring.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //get the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");

        //verifying the authHeader and check it startsWith 'Bearer'
        if(authHeader==null || !authHeader.startsWith("Bearer")){
            //then do nothing and invoke the username password filter
            filterChain.doFilter(request, response);
            return;
        }
        //else get the jwt token from the header value which starts from index 7
        final String jwtToken = authHeader.substring(7);

        //extract user name from jwt token
        final String userName = jwtService.extractUserName();

        //getting authentication object from security context
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();

        // if authentication is not done,  setting the authentication
        if(userName !=null && authentication==null){
            UserDetails userDetails
                    = userDetailsService.loadUserByUsername(userName);
            if(jwtService.isValidToken(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)

                );
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }
        }
        else
            filterChain.doFilter(request, response);


    }
}
