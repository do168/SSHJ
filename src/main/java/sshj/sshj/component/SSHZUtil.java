package sshj.sshj.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SSHZUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean isLoginReg(String str) {
        if(!str.matches("^[a-zA-Z0-9]*$")) {
            log.info("아이디는 영문 혹은 숫자로만 가능합니다.");
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isEmailReg(String str) {
      if(str.split("@")[1].equals("uos.ac.kr"))
          return true;

      else {
          log.info("서울시립대 이메일이 아닙니다.");
          return false;
      }
    }

    public boolean isValidCode(long t1, long t2) {
        if (t1 - t2 <= 3000) {
            log.info("유효한 코드");
            return true;
        }
        else {
            log.info("우효하지 않은 코드");
            return false;
        }
    }

    public void afterLogoutToken(String token) {
        redisTemplate.opsForValue().set(token, true);
        redisTemplate.expire(token, 30 * 60 * 1000L, TimeUnit.MILLISECONDS);
    }
}
