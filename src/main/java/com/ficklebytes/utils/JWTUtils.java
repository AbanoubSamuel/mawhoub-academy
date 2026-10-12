package com.ficklebytes.utils;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import com.auth0.jwt.JWT;

import java.util.Date;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class JWTUtils {

    public static final String SECRET = System.getProperty("app.jwt.secret", System.getenv("JWT_SECRET"));
    public static final long EXPIRATION_PERIOD = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String IP_ADDRESS_CLAIM_NAME = "ip";
    public static final String INVALID_IP_ADDRESS_ATTRIBUTE_NAME = "invalid ip";
    public static final String EXPIRED_ATTRIBUTE_NAME = "expired";
    public static final String TOKEN_CHANGED_ATTRIBUTE_NAME = "token changed";
    public static String createToken(String username, String ip) {
        final String token =  JWT.create()
                .withSubject(username)
                .withClaim(IP_ADDRESS_CLAIM_NAME, ip)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_PERIOD))
                .sign(HMAC512(SECRET.getBytes()));
        log.info(String.format("%s issued new token %s", username, token));
        return token;

    }
    public static String extractClaim(String token,String claimName) {
        return          JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token.replace(TOKEN_PREFIX, ""))
                        .getClaim(claimName).asString();

    }
}
