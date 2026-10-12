package com.ficklebytes.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ficklebytes.entity.AcademyUser;

import com.ficklebytes.enums.Role;
import com.ficklebytes.exception.ErrorMessages;
import com.ficklebytes.service.UserDetailsServiceImpl;
import com.ficklebytes.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.ficklebytes.exception.ErrorMessages.IP_ADDRESS_CHANGED;
import static com.ficklebytes.exception.ErrorMessages.TOKEN_EXPIRED;
import static com.ficklebytes.utils.JWTUtils.*;

@Log4j2
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserDetailsServiceImpl userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authManager,
                                  UserDetailsServiceImpl userDetailsService) {
        super(authManager);
        this.userDetailsService = userDetailsService;
        System.out.println("JWTAuthorizationFilter constructor called");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException,
            ServletException {
        System.out.println("=== JWT Filter processing: " + req.getRequestURI() + " ===");
        log.info("JWT Filter processing: " + req.getRequestURI());
        
        try {
            String header = req.getHeader(AUTHORIZATION_HEADER_NAME);
            System.out.println("Authorization header: " + (header != null ? "present" : "null"));
            log.info("Authorization header: " + (header != null ? "present" : "null"));
            
            if (header == null || !header.startsWith(TOKEN_PREFIX)) {
                // No authorization header, continue with the filter chain
                System.out.println("No valid authorization header, continuing...");
                log.info("No valid authorization header, continuing...");
                chain.doFilter(req, res);
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    getAuthentication(req);
            if (req.getAttribute(EXPIRED_ATTRIBUTE_NAME) != null) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, TOKEN_EXPIRED);
                return;
            }
            if (req.getAttribute(INVALID_IP_ADDRESS_ATTRIBUTE_NAME) != null) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, IP_ADDRESS_CHANGED);
                return;
            }
            if (req.getAttribute(TOKEN_CHANGED_ATTRIBUTE_NAME) != null) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessages.TOKEN_CHANGED);
                return;
            }
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            
            chain.doFilter(req, res);
        } catch (Exception e) {
            log.error("Error in JWT filter: " + e.getMessage(), e);
            // Continue with the filter chain even if there's an error
            chain.doFilter(req, res);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER_NAME);

        try {
            if (token != null) {
                token = token.replace(TOKEN_PREFIX, "");

                // verify and extract subject (username)
                String username = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token)
                        .getSubject();

                if (username != null) {
                    try {
                        // load user from DB
                        AcademyUser userDetails = (AcademyUser) userDetailsService.loadUserByUsername(username);

                        // === Security checks for ADMIN users ===
                        if (userDetails.getRole() == Role.ADMIN) {
                            String ipAddressFromRequest = Utils.getRemoteAddress(request);
                            String ipAddressFromToken = extractClaim(token, IP_ADDRESS_CLAIM_NAME);

                            if (!ipAddressFromRequest.equals(ipAddressFromToken)) {
                                request.setAttribute(INVALID_IP_ADDRESS_ATTRIBUTE_NAME,
                                        INVALID_IP_ADDRESS_ATTRIBUTE_NAME);
                            }

                            if (!token.equals(userDetails.getToken())) {
                                log.info("Token has been changed oldToken={} newToken={}", token, userDetails.getToken());
                                request.setAttribute(TOKEN_CHANGED_ATTRIBUTE_NAME, TOKEN_CHANGED_ATTRIBUTE_NAME);
                            }
                        }

                        // ✅ Correct: principal = userDetails, with real authorities
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities() // use authorities from DB/UserDetails
                                );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        return authentication;

                    } catch (Exception e) {
                        log.warn("User not found or error loading user: {}", username, e);
                        return null;
                    }
                }
            }
        } catch (TokenExpiredException tokenExpiredException) {
            request.setAttribute(EXPIRED_ATTRIBUTE_NAME, tokenExpiredException.getMessage());
        } catch (SignatureVerificationException ignored) {
            // invalid signature
        }
        return null;
    }

}
