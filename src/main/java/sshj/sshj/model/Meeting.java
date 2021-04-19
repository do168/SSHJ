package sshj.sshj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "meeting")
@Data
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "startDate")
    private LocalDateTime startDate;

    @Column(name = "endDate")
    private LocalDateTime endDate;

    @Column(name = "category")
    private String category;

    @Column(name = "explanationTitle")
    private String explanationTitle;

    @Column(name = "explanationContent")
    private String explanationContent;

    @OneToMany(mappedBy = "meeting")
    private List<Image> imgs;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "maxParticipant")
    private int maxParticipant;

    @Column(name = "explanationContent")
    private String explanationContent;

    @Column(name = "place")
    private String place;

    @Column(name = "chatUrl")
    private String chatUrl;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("club", club.toString())
                .append("deadline", deadline)
                .append("startDate", startDate)
                .append("endDate", endDate)
                .append("category", category)
                .append("explanationTitle", explanationTitle)
                .append("explanationContent", explanationContent)
                .append("imgs", imgs.toString())
                .append("createdAt", createdAt)
                .append("updatedAt", updatedAt)
                .append("maxParticipant", maxParticipant)
                .append("place", place)
                .append("chatUrl", chatUrl)
                .toString();
    }

}
