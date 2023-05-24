package example.CodeInterviewPrepAPI;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemLogRepository extends JpaRepository<ProblemLog, Long> {
}