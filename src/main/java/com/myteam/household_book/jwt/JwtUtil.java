package com.myteam.household_book.jwt;

import com.myteam.household_book.security.CustomUserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final String issuer;
    private final long accessTtlSeconds;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.access-ttl-seconds:86400}") long accessTtlSeconds
    ) {
        // 항상 같은 키를 사용 (랜덤 키 금지)
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.accessTtlSeconds = accessTtlSeconds;
    }

    /** userId(uid) + username(sub) 포함해서 발급 */
    public String generateToken(Long userId, String username) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessTtlSeconds);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(username)             // sub: username(또는 email로 바꿔도 됨)
                .claim("uid", userId)             // 커스텀 클레임: 우리 시스템 userId
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 유효성만 확인 */
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer(issuer)
                    .setAllowedClockSkewSeconds(60)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /** 토큰 → CustomUserPrincipal (SecurityContext 주입에 사용) */
    public CustomUserPrincipal parseAndBuildPrincipal(String token) throws JwtException {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer)
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token);

        Claims c = jws.getBody();
        Long uid = c.get("uid", Number.class).longValue();
        String sub = c.getSubject();
        return new CustomUserPrincipal(uid, sub);
    }

    /** 편의 메서드들 */
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
    public String getSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).requireIssuer(issuer).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
