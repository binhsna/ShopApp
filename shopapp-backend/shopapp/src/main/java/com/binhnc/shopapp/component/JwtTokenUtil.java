package com.binhnc.shopapp.component;

import com.binhnc.shopapp.exception.InvalidParamException;
import com.binhnc.shopapp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoder;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration; // Save to an environment variable
    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws Exception {
        // Properties => claims: Thuộc tính
        Map<String, Object> claims = new HashMap<>();
        // this.generateSecretKey();
        // 6QljiUkU0sXBCBy+YJ2YC4Sk/Lqjro/RNqQgGw5KLOI=
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            String token = Jwts.builder()
                    .setClaims(claims) // How to extract claims form this?
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKeys(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            // You can inject Logger, instead System.out.println
            // System.err.println("Cannot create jwt token, error: " + e.getMessage());
            throw new InvalidParamException("Cannot create jwt token, error: " + e.getMessage());
            // return null;
        }
    }

    private Key getSignInKeys() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    // Random create SecretKey
    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];// 256-Bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    private Claims extracAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKeys())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extracAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Check expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
}
