package sshj;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SshjApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml";

	
	public static void main(String[] args) {
		
		new SpringApplicationBuilder(SshjApplication.class)
			.properties(APPLICATION_LOCATIONS)
			.run(args);

	}

}
