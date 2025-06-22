package com.shriva.personal_finance_manager_backend_java.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.debug("Extracted token: {}", token);

            if (token.startsWith("temp-jwt-bypass-")) {
                String username = token.replace("temp-jwt-bypass-", "");
                logger.info("Bypass token detected, extracted username: {}", username);

                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.info("Loaded user: {}", username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Authenticated user: {}", username);
                } catch (Exception e) {
                    logger.error("Authentication failed for user {}: {}", username, e.getMessage());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid bypass token or user not found");
                }
            } else {
                // Handle regular JWT token logic if needed (currently commented out)
                // String username = jwtUtil.getUsernameFromToken(token); // Uncomment and add jwtUtil if using JWT
                // if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //     UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //     if (jwtUtil.validateToken(token)) {
                //         UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                //                 userDetails, null, userDetails.getAuthorities());
                //         authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //         SecurityContextHolder.getContext().setAuthentication(authentication);
                //     } else {
                //         response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT Token");
                //     }
                // }
            }
        }

        filterChain.doFilter(request, response);
    }
}