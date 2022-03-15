package net.therap.mealsystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sheikh.ishrak
 * @since 09/03/2022
 */
public class JwtUtil {

    public static final String SECRET_KEY = "secretkeysecretkeysecretkeysecretkey";
    public static final long TOKEN_EXPIRY = 30 * 60 * 1000;

    public static Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(JwtUtil.SECRET_KEY.getBytes()))
                .parseClaimsJws(token).getBody();
    }

    public static List<Map<String, String>> getAuthorities(String token) {
        return (List<Map<String, String>>) getAllClaims(token).get("authorities");
    }

    public static String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public static Date getTokenExpiration(String token) {
        return getAllClaims(token).getExpiration();
    }

    public static boolean isTokenInvalid(String token) {
        return (isTokenExpired(token));
    }

    public static boolean isTokenExpired(String token) {
        return getTokenExpiration(token).before(new Date());
    }

    public static String generateToken(Authentication authResult) {
        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRY))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).compact();
    }
}
