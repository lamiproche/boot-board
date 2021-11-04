package org.zerock.sb.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    //The specified key byte array is 64 bits which is not secure enough for any JWT HMAC-SHA algorithm.
    //version이 올라가면서 key값이 너무 짧으면 error 발생함
    private final static String secretKey = "helloworld111112222233333333344444444444555555555";

    //javax의 암호화 라이브러리
    private SecretKey key;

    public JWTUtil() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //1. generate 작업 필요
    public String generateToken(String content) {

        long timeAmount = 2;  //분단위

        String jws = Jwts.builder()  // (1)
                .setSubject(content)  // (2)
                .setIssuedAt(new Date())  /*언제 발행?*/
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(timeAmount).toInstant()))  /*언제까지? timeAmount - 유효기간*/
                .signWith(key, SignatureAlgorithm.HS256)  // (3) 키값 부여
                .compact();  // (4) 발행

        return jws;
    }

    //2. JWT check
    public void validateToken(String token) throws JwtException {

        Jws<Claims> jws = Jwts.parserBuilder()  // (1)
                .setSigningKey(key)         // (2)
                .build()                    // (3)
                .parseClaimsJws(token); // (4)

    }

}