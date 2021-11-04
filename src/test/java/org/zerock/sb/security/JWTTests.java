package org.zerock.sb.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.security.util.JWTUtil;

@SpringBootTest
@Log4j2
public class JWTTests {

    @Autowired
    JWTUtil jwtUtil;

    @Test
    public void testGenerate() {

        String jwtStr = jwtUtil.generateToken("user11");

        log.info(jwtStr);
    }

    @Test
    public void testValidate() {
        //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTEiLCJpYXQiOjE2MzQ4NzA5OTksImV4cCI6MTY2NjQwNjk5OX0.p7JWF-TfqTG3WQtJcVi4kKgi6JmY8UE8v2TBRTVbxrE
        String str = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTEiLCJpYXQiOjE2MzQ4NzA5OTksImV4cCI6MTY2NjQwNjk5OX0.p7JWF-TfqTG3WQtJcVi4kKgi6JmY8UE8v2TBRTVbxrE";
        String wrongStr = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTEi2345678LCJpYXQiOjE2MzQ4NjkwNzksImV4cCI6MTYzNDg3MjY3OX0.q1AHB9RpTTJQFWNyYeGwY4MB1DLqhfRvt6B-OosXavI";
        String timeoutStr = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTEiLCJpYXQiOjE2MzQ4NzA0NTgsImV4cCI6MTYzNDg3MDQ1OH0.R0g2mXzCMuqdWAz9K9IxHsEknZefrme7ke3sjTzOofI";
        //JWT 사이트에서는 맞다고 했지만 우리 메소드를 통해서도 검증이 되는지
        //잘못된, 만료된 토큰을 넣었을 때 제대로 결과가 나오는지 체크해야함

        try {
//            jwtUtil.validateToken(str);
//            jwtUtil.validateToken(wrongStr);
            jwtUtil.validateToken(timeoutStr);
        } catch (ExpiredJwtException ex) {
            log.info("expired.............");
            log.error(ex.getMessage());
        } catch (JwtException ex) {
            log.info("JWTException.............");
            log.error(ex.getMessage());
        }
    }
}
