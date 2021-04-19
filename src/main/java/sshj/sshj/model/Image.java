package sshj.sshj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "img")
public class Image {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
