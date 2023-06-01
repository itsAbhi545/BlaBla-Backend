package com.example.BlaBlaBackend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private String jwtSecret="sIoVC8OFOgmxbk9XRYtY2zMKXuYXBGL2d3x1IV37";
//    private Long jwtExpiration = 6000000L;
    private Long jwtExpiration = 600000000000000L;

    private Claims parseToken(String token) {
        // Create JwtParser
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build();
        try {
            return jwtParser.parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean validateToken(String token) {
        return parseToken(token) != null;
    }

    public String getUsernameFromToken(String token) {
        // Get claims
        Claims claims = parseToken(token);
        //System.out.println(claims.get("role")+"////");
        // Extract subject
        if(claims != null){
            return claims.getSubject();
            //return (String) claims.get(key);
        }
        return null;
    }

    public String generateToken(String email) {
        // Create signing key
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        // Generate token
        var currentDate = new Date();
        var expiration = new Date(currentDate.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
              //  .claim("role",role)
                .setIssuedAt(currentDate)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }
    public Claims extractAllClaims(String token) {
        System.out.println(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody());
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}
