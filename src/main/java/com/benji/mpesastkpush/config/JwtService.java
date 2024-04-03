package com.benji.mpesastkpush.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    private static final String SECRET_KEY="C7EA44E5DBA094DC3288D2BB35EC25C09F7421DE2DCA04C3F4600B76";
    public String extractUserName(String token) {

        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim (String token, Function<Claims,T> claimsResolver){
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public  String generateToken (UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        Date now = new Date();
        log.info("Date is now" + now);
        Date expiryDate = new Date(now.getTime() + 24 * 60 * 60 * 1000); // Expiry in 24 hours
        log.info("expiryDate" + expiryDate);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username= extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpiredToken(token));
    }

    private boolean isTokenExpiredToken(String token) {
        return (extractExpiration(token).before(new Date()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
