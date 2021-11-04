package org.zerock.sb.security.filter;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.zerock.sb.dto.MemberDTO;
import org.zerock.sb.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
public class TokenGenerateFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public TokenGenerateFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        //defaultFilterProcessesUrl - 로그인 경로
        super(defaultFilterProcessesUrl, authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        //JSON 문자열 얻어오기
        String requestStr = extracted(request);

        log.info("try to login with json for api...............");
        log.info(requestStr);

        JSONObject jObject = new JSONObject(requestStr);

        String userId = jObject.getString("userId");
        String password = jObject.getString("password");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userId, password);

        Authentication result = getAuthenticationManager().authenticate(authToken);

        log.info("---------------------------");
        log.info(result);

        return result;
    }

    // request를 JSON화 해주는 메소드

    private String extracted(HttpServletRequest request)  {
        InputStream inputStream = null;
        ByteArrayOutputStream bos = null;

        try {
            inputStream = request.getInputStream();
            bos = new ByteArrayOutputStream();
            byte[] arr = new byte[1024];

            while (true) {
                int count = inputStream.read(arr);
                if (count == -1) {
                    break;
                }
                bos.write(arr, 0, count);
            }
        }catch(Exception e){

        }finally {
            try{inputStream.close(); }catch(Exception e){}
            try {bos.close();}catch(Exception e){}
        }
        return bos.toString();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //인증 성공 시 - 사용자 정보를 가지고 access token을 생성해 JSON으로 전송
        log.info("successfulAuthentication: " + authResult);
        //아예 인증된 정보 자체가 넘어옴

        MemberDTO memberDTO = (MemberDTO) authResult.getPrincipal();
        //이제 내가 원하는 정보만 추출해서 토큰 생성하면 됨
        //회원 ID / 어드민과 구별?
        //API자체가 이미 허가된 사용자가 받는거라 큰 의미는 xX

        String mid = memberDTO.getMid();
        log.info("MEMBER MID: " + mid);

        String token = jwtUtil.generateToken(mid);  //토큰 생성
        //이제 전송하면됨

        JSONObject res = new JSONObject(Map.of("ACCESS", token));

        response.setContentType("application/json");
        PrintWriter out  = response.getWriter();
        out.println(res.toString());
        out.close();

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("unsuccessfulAuthentication: " + failed);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // json 리턴
        response.setContentType("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        String message = failed.getMessage();
        json.put("code", "401");
        json.put("message", message);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.close();
    }

}
