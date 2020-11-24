package sshj.sshj.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResultModel {

    private String msg;

    private Boolean flag;
}
