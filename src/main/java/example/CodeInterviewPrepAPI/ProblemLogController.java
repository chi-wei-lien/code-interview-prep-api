package example.CodeInterviewPrepAPI;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Optional;

@RestController
@RequestMapping("/problemlog")
public class ProblemLogController {
    private ProblemLogRepository problemLogRepository;

    public ProblemLogController(ProblemLogRepository problemLogRepository) {
        this.problemLogRepository = problemLogRepository;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<ProblemLog> findById(@PathVariable Long requestedId) {
        Optional<ProblemLog> problemLogOptional = problemLogRepository.findById(requestedId);

        if (problemLogOptional.isPresent()) {
            return ResponseEntity.ok(problemLogOptional.get());
        } else {
            System.out.println("didn't find requested id");
            throw new ProblemLogNotFoundException(requestedId);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<ProblemLog> newCashCard(@RequestBody ProblemLog problemLog) {
        ProblemLog savedProblemLog = problemLogRepository.save(problemLog);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<ProblemLog>(savedProblemLog, responseHeaders, HttpStatus.CREATED);
    }
}
