package example.CodeInterviewPrepAPI.Repositories;

import example.CodeInterviewPrepAPI.Models.ProblemLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemLogRepository extends JpaRepository<ProblemLog, Long> {
}