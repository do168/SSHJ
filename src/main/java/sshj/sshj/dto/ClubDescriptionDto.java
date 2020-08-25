package sshj.sshj.dto;

import lombok.Data;

@Data
public class ClubDescriptionDto {
    private int clubId;

    private String description;

    public ClubDescriptionDto(int clubId, String description) {
        this.clubId = clubId;
        this.description = description;
    }
}
