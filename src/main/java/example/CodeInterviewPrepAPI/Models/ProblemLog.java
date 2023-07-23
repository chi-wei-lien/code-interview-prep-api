package example.CodeInterviewPrepAPI.Models;

import jakarta.persistence.*;

import java.util.Objects;
import java.time.OffsetDateTime;

@Entity
@Table(name = "problem_logs")
public class ProblemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double difficulty;
    private String url;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;

    public ProblemLog() {}

    public ProblemLog(String name, Double difficulty, String url, OffsetDateTime timestamp) {
        this.name = name;
        this.difficulty = difficulty;
        this.url = url;
        this.timestamp = timestamp;
    }

    public ProblemLog(Long id, String name, Double difficulty, String url, OffsetDateTime timestamp) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.url = url;
        this.timestamp = timestamp;
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public String getUrl() {
        return url;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(Double difficulty) {
        this.difficulty = difficulty;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object incomingObject) {
        if (this == incomingObject) return true;
        if (!(incomingObject instanceof ProblemLog)) return false;
        ProblemLog incomingProblemLog = (ProblemLog) incomingObject;

        return Objects.equals(this.id, incomingProblemLog.id) && 
                Objects.equals(this.name, incomingProblemLog.name) &&
                Objects.equals(this.difficulty, incomingProblemLog.difficulty) &&
                Objects.equals(this.url, incomingProblemLog.url);
    }

    @Override
    public String toString() {
        return String.format("ProblemLog{id=%l,name=%s,difficulty=%d,url=%s,timestamp=%s}", 
                id, name, difficulty, url, timestamp.toString());
    }
}
