package sshj.sshj.dto;

import lombok.Data;

@Data
public class UserHeaderModel {
    private long userId;

    private String email;

    private String role;

    private String nickname;
}
