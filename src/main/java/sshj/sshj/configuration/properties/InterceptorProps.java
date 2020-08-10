package sshj.sshj.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Repository
@ConfigurationProperties("interceptor")
@Data
@EqualsAndHashCode(callSuper=false)
public class InterceptorProps {

    private InterceptorMappingProps authCheck;
}

