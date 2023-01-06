package com.seris.bassein.service.security.config;

import com.seris.bassein.service.security.model.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final long JWT_TOKEN_VALIDITY = 12 * 3600;
    @Value("${jwt.secret}")
    private String secret;

    //jwt токеноос хэрэглэгчийн нэрийг татаж авах
    public String getUsernameFromToken(String token) {
        if (token == null || token.isEmpty()) return "not authenticated user";
        return getClaimFromToken(token, Claims::getSubject);
    }

    //jwt токеноос дуусах хугацааг авах
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //аливаа мэдээллийг авахын тулд нууц түлхүүр хэрэгтэй болно
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //token хугацаа дууссан эсэхийг шалгах
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //хэрэглэгчид токен үүсгэх
    public JwtResponse generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private JwtResponse doGenerateToken(Map<String, Object> claims, String subject) {
        LocalDateTime expiredDate = LocalDateTime.now().plusSeconds(JWT_TOKEN_VALIDITY);
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(expiredDate.atZone(java.time.ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return new JwtResponse(token, "Bearer", expiredDate);
    }

    //токенийг баталгаажуулах
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
