package com.example.messengerapplication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtService(@Value("${app.jwt.secret}")String secretKey,@Value("${app.jwt.expiration-ms}") long expirationTime) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);;
        this.expirationTime = expirationTime;
    }

    public String generateToken(String email) {
           Date now = new Date();
           Date expiryDate = new Date(now.getTime() + expirationTime);

           return Jwts.builder().
                   subject(email).
                   issuedAt(now).
                   expiration(expiryDate).
                   signWith(secretKey).
                   compact();
    }


    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }


    public boolean isTokenValid(String token) {
        try{
            Claims claims = extractClaims(token);
            return claims.getSubject()!=null &&
                    claims.getExpiration()!=null&&
                    claims.getExpiration().after(new Date());
        }catch(Exception e){
            return false;
        }
    }


    private Claims extractClaims(String token) {
        return Jwts.parser().
                verifyWith(secretKey).
                build().
                parseSignedClaims(token).
                getPayload();
    }


}
