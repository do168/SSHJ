package sshj.sshj.dto;

import lombok.Data;

@Data
public class UserHeaderModel {
    private int userId;

    private String loginId;

    private String role;

    private String nickname;
}
