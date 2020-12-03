package sshj.sshj.configuration;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sshj.sshj.dto.UserDto;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
	
	@Value("${spring.profiles.active}")
	private String activeProfile;
	
    private String secretKey = "DaeDoCrew";

    // 토큰 유효시간 30분
    private long access_tokenValidTime = 6 * 60 * 60 * 1000L;

    private long refresh_tokenValidTime =14 * 24 * 60 * 60 * 1000L;

    @Autowired
    private RedisTemplate redisTemplate;
    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createAccessToken(String userPk, long userId, String nickname, String role) {
        List<String> roles = new ArrayList<>();
        roles.add(role);
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        claims.put("userId", userId);
        claims.put("nickname", nickname);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + access_tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + refresh_tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
//        log.info("PK : ",this.getUserPk(token));
//        UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token)); // 계속 DB 검사 -> 토큰 쓰는 이유가 없다 -> 고쳐야 한다.
        UserDto userDto = new UserDto();
        Claims parseInfo = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        userDto.setEmail(parseInfo.getSubject());
        userDto.setRole(parseInfo.get("roles", List.class).toString().substring(1,parseInfo.get("roles", List.class).toString().length()-1));
        userDto.setUserId(parseInfo.get("userId", Integer.class));
        userDto.setNickname(parseInfo.get("nickname", String.class));
        return new UsernamePasswordAuthenticationToken(userDto, "", userDto.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        try{
            String userPk = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            return userPk;
        } catch (IllegalArgumentException e) { // 메소드의 매개변수 사용을 잘못했을 때. 즉 사용했을 때 파서가 안되는 경우
            log.info("token cannot parser by this secretKey");
            return null;
        } catch (ExpiredJwtException e) { //expire됐을 때
            String email = e.getClaims().getSubject();
            log.info("Email from expired access token: " + email);
            return null;
        }
    }
    public UserDto getUserDto(String token) {
        Claims parseInfo = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        UserDto userDto = new UserDto();
        userDto.setEmail(parseInfo.getSubject());
        userDto.setUserId(parseInfo.get("userId", Integer.class));
        userDto.setRole(parseInfo.get("roles", List.class).toString().substring(1,parseInfo.get("roles", List.class).toString().length()-1));
        return userDto;
    }



    // Request의 Header에서 token 값을 가져옵니다. "authorization" : "TOKEN값'
    // access_token과 refresh_token을 따로 받아오는 알고리즘 필요
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            // 이 부분 고칠필요가 있다. secretKey로 복호화하는데, 엉뚱한 토큰을 보낸다면 Signature에서 걸러줄 필요가 있다.
            // 그 부분 추가가 필요하다.
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            // 블랙리스트에 access_token이나 refresh_token이 존재하는지 체크, 존재한다면 로그아웃한 토큰이므로 통과 X
            boolean inblacklist = true;
//            if("dev".equals(activeProfile))
            	inblacklist = redisTemplate.opsForValue().get(jwtToken) != null;
//            else if("local".equals(activeProfile)) {
//            	log.info("Profile [local] token");
//            	inblacklist = false;
//            }
            return !claims.getBody().getExpiration().before(new Date()) && !inblacklist;
        } catch (Exception e) {
            return false;
        }
    }
}
