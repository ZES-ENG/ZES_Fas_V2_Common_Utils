package com.zes.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    // Access 토큰 생성
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(long userId, long companyId, long expirationTime) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expireDate = new Date(nowMillis + expirationTime);
        SecretKey signingKey = getSigningKey();
        Map<String,Long> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("companyId", companyId);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // 헤더
                .setIssuer("ZESTECH_FAS_V2") // 발급자
                .setClaims(claims)
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expireDate) // 만료 시간
                .signWith(signingKey, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        Date expirationDate = claims.getExpiration(); // `exp` 클레임 가져오기
        return expirationDate.before(new Date()); // 현재 시간과 비교하여 만료 여부 확인
    }

    public String generateAccessToken(long userId, long companyId, long expirationTime) {
        log.info("Generating access token for userPk: {}", userId);
        String token = createToken(userId, companyId, expirationTime);
        return token;
    }

    private String encodeToken(String token) {
        try {
            return URLEncoder.encode("Bearer " + token, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error encoding token: {}", e.getMessage(), e);
            throw new RuntimeException("Error encoding token", e);
        }
    }

    public Claims validateToken(String token) throws SignatureException {
        if (token == null || !token.startsWith("Bearer ")) {
            log.error("Invalid token format: {}", token);
            throw new SignatureException("Invalid token");
        }

        String parsedToken = token.substring(7);
        log.info("Validating token: {}", parsedToken);

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(parsedToken)
                    .getBody();
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage(), e);
            throw new SignatureException("Token validation failed", e);
        }
    }

    public String extractUsername(Claims claims) {
        String username = claims.getSubject();
        log.info("Extracted username: {}", username);
        return username;
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 서명 검증
                .build()
                .parseClaimsJws(token) // 토큰 해석
                .getBody(); // Claims(페이로드) 가져오기
    }

}
