package sshj.sshj.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry cr) {
		cr.addMapping("/**").allowedOrigins("*").allowedOrigins("http://localhost:9090") // 허용할 주소 및 포트
				.allowCredentials(true).allowedHeaders("*").allowedMethods("DELETE", "POST", "GET", "OPTIONS");
		// allowed method 는 default로 GET, POST, DELETE 만 설정 PUT을 직접넣어줘야함
	}
}
