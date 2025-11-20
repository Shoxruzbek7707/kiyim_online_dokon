package uz.pdp.kiyim_online_dokon.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    private final String secretKeyWord = UUID.randomUUID().toString();

    public String generateToken(String username){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + 3600 * 1000);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(generateKey(),SignatureAlgorithm.HS256).compact();
    }

    private Key generateKey(){
        return Keys.hmacShaKeyFor(secretKeyWord.getBytes());
    }

    public boolean validateToken(String token){
        try{
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = body.getExpiration();
            return expiration.after(new Date());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getSubject(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(token).getBody().getSubject();
        }catch (Exception e){
            throw new RuntimeException("User not found");
        }
    }
}
