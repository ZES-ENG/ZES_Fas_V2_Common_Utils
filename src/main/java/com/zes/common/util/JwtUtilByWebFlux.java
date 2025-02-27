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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ConditionalOnMissingBean(JwtUtilByServlet.class)
public class JwtUtilByWebFlux {

    public Optional<String> getTokenFromCookieByWebflux(ServerHttpRequest request, String cookieName) {
        return Optional.ofNullable(request.getCookies().getFirst(cookieName))
                .map(HttpCookie::getValue);
    }

}
