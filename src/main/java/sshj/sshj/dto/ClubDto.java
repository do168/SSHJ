package sshj.sshj.dto;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import sshj.sshj.model.Club;

import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class ClubDto {

    private long id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    protected ClubDto() {/*empty*/}

    public ClubDto(Club club) {
        copyProperties(club, this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("createdAt", createdAt)
                .toString();
    }

}
