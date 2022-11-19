package com.example.g31_ffs_be.security;

import com.example.g31_ffs_be.exception.JwtException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("congbvhe")
    private String jwtSecret;

    @Value("604800000")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new JwtException(HttpStatus.BAD_REQUEST, "JWT signature không hợp lệ");
        } catch (MalformedJwtException ex) {
            throw new JwtException(HttpStatus.BAD_REQUEST, "Jwt token không hợp kệ");
        } catch (ExpiredJwtException ex) {
            throw new JwtException(HttpStatus.BAD_REQUEST, "Jwt token quá hạn");
        } catch (UnsupportedJwtException ex) {
            throw new JwtException(HttpStatus.BAD_REQUEST, "Unsupported JWT signature");
        } catch (IllegalArgumentException ex) {
            throw new JwtException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }

}
