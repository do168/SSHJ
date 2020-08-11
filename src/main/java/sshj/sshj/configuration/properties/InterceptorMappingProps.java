package sshj.sshj.configuration.properties;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DDC-hoon
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InterceptorMappingProps {

    private String interceptorName;

    private String[] mappings;

    private String[] excludeMappings;

    private int order;
}
