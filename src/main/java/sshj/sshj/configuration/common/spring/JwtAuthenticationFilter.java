package sshj.sshj.configuration.common.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.GenericFilterBean;
import sshj.sshj.configuration.JwtTokenProvider;
import sshj.sshj.dto.UserDto;
import sshj.sshj.dto.UserHeaderModel;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {


    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            // 원래는 Userservice 를 통해 디비에 저장된 유저 정보와 비교를 해야하지만 이는 토큰 사용의 장점과 모순되는 점이 많음
            // 따라서 유효성만을 검사한 후 Authentication 객체를 만들어 SecurityContextHolder에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDto userDto = (UserDto) authentication.getPrincipal();
            log.info("in filter, userDto userId : "+userDto.getUserId());
            UserHeaderModel userHeaderModel = new UserHeaderModel();
            userHeaderModel.setEmail(userDto.getEmail());
            userHeaderModel.setRole(userDto.getRole());
            userHeaderModel.setUserId(userDto.getUserId());
            // HttpServletRequest에 생성한 UserHeaderModel 객체를 담아 보낸다.
            // 다른 api에서 @ReqeustAttribute Annotation을 통해 정보 사용이 가능하다
            request.setAttribute("UserHeaderInfo", userHeaderModel);
        }
        chain.doFilter(request, response);
    }
}