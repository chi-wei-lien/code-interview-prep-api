package example.CodeInterviewPrepAPI;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.util.Objects;
import java.time.OffsetDateTime;

public class ProblemLog {

    private @Id @GeneratedValue Long id;
    private String name;
    private Double difficulty;
    private String url;

    @Column(name = "offset_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime timestamp;

    public ProblemLog(String name, Double difficulty, String url) {
        this.name = name;
        this.difficulty = difficulty;
        this.url = url;
    }

    public ProblemLog(Long id, String name, Double difficulty, String url) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.url = url;
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

    public String url() {
        return url;
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
        return String.format("ProblemLog{id=%l,name=%s,difficulty=%d,url=%s}", id, name, difficulty, url);
    }
    
}
