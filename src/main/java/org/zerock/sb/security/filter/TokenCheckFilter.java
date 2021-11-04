package org.zerock.sb.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.sb.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class TokenCheckFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public TokenCheckFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //시작
        log.info("---------- TokenCheckFilter ----------");
        log.info("---------- TokenCheckFilter ----------");

        String path = request.getRequestURI();  //어디서 호출하는지
        log.info(path);

        if (path.startsWith("/api222/")) {
            //api로 들어오면 check token
            //공식 HTTP Authorization token 사용
            String authToken = request.getHeader("Authorization");

            if (authToken == null) {
                //이게 null이면 토큰 발급받고 오라고 보내야함
                log.info("authToken is null............");

                //1. 여기서 메세지 만들어서 보내기VV / 2. Forward 이용해서 보내기
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;
            }

            //jwt 검사 - 맨 앞에 인증 타입 존재 (Bearer)
            String tokenStr = authToken.substring(7);  //Bearer + 공백


            try {
                jwtUtil.validateToken(tokenStr);
                //검사를 했는데 예외발생? -> 토큰에 문제 있다는 것
            } catch (ExpiredJwtException ex) {
                //인증만료토큰
                log.info("user refresh token..too old.. start from login... !!!");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "EXPIRED API TOKEN.. TOO OLD";
                json.put("code", "401");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;

            } catch (JwtException jex) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "YOUR ACCESS TOKEN IS INVALID";
                json.put("code", "401");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;

            }

            filterChain.doFilter(request, response);

        } else {
            log.info("========== TokenCheckFilter ==========");
            //정상적으로 왔으니 다음단계로 진행시키는 기능
            //문제가 생길 경우 여기로 연결시키면 안되고 튕겨내야함 - JSON Object 사용
            filterChain.doFilter(request, response);
        }

    }

}
