package sshj.sshj.configuration;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Profile({"local", "dev"})
@Configuration
@EnableSwagger2
@EnableWebMvc
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration implements WebMvcConfigurer {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Profile("local")
    @Bean
    public Docket localApi() {
        String info = "-----------------------------\n"
                + "-- swagger api for [local] --\n"
                + "-----------------------------\n";
        return getDefaultDocket(null, null);
    }

    @Profile("dev")
    @Bean
    public Docket devApi() {
        String info = "-----------------------------\n"
                + "-- swagger api for [dev] --\n"
                + "-----------------------------\n";
        return getDefaultDocket(null, null);
    }



    private Docket getDefaultDocket(Set<String> protocols, String host){
        log.debug("Starting Swagger");
        ApiInfo apiInfo = new ApiInfoBuilder().title("sshj-api").description("SSHJ API").build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        if(protocols != null) {
            docket.protocols(protocols);
        }

        if(host != null) {
            docket.host(host);
        }

        List global = new ArrayList();
//        global.add(new ParameterBuilder().name("Authorization").
//                description("Access Token").parameterType("header").
//                required(false).modelRef(new ModelRef("string")).build());

        return docket
                .globalOperationParameters(global)
                .select()
                .apis(RequestHandlerSelectors.basePackage("sshj.sshj.controller"))
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(apiInfo)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()));

    }


    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
    }

}
