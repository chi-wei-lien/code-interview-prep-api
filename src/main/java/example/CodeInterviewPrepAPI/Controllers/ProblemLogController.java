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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Optional;

@RestController
@RequestMapping("/problemlog")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProblemLogController {
    private ProblemLogRepository problemLogRepository;

    public ProblemLogController(ProblemLogRepository problemLogRepository) {
        this.problemLogRepository = problemLogRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<ProblemLog>> findByPage(Pageable pageable) {
        Page<ProblemLog> page = problemLogRepository.findAll(
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.DESC, "timestamp"))));
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<ProblemLog> findById(@PathVariable Long requestedId) {
        Optional<ProblemLog> problemLogOptional = problemLogRepository.findById(requestedId);

        if (problemLogOptional.isPresent()) {
            return ResponseEntity.ok(problemLogOptional.get());
        } else {
            throw new ProblemLogNotFoundException(requestedId);
        }
    }

    @PostMapping
    public ResponseEntity<ProblemLog> newProblemLog(@RequestBody ProblemLog problemLog) {
        ProblemLog savedProblemLog = problemLogRepository.save(problemLog);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<ProblemLog>(savedProblemLog, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<ProblemLog> updateProblemLog(@RequestBody ProblemLog problemLog, @PathVariable Long requestedId) {
        Optional<ProblemLog> problemLogOptional = problemLogRepository.findById(requestedId);

        if (problemLogOptional.isPresent()) {
            ProblemLog currProblemLog = problemLogOptional.get();
            currProblemLog.setName(problemLog.getName());
            currProblemLog.setDifficulty(problemLog.getDifficulty());
            currProblemLog.setUrl(problemLog.getUrl());
            currProblemLog.setTimestamp(problemLog.getTimestamp());
            problemLogRepository.save(currProblemLog);
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<ProblemLog>(responseHeaders, HttpStatus.NO_CONTENT);

        } else {
            ProblemLog savedProblemLog = problemLogRepository.save(problemLog);
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<ProblemLog>(savedProblemLog, responseHeaders, HttpStatus.CREATED);
        }   
    }

    @DeleteMapping("/{requestedId}")
    public  ResponseEntity<ProblemLog> deleteProblemLog(@PathVariable Long requestedId) {
        Optional<ProblemLog> problemLogOptional = problemLogRepository.findById(requestedId);
        if (problemLogOptional.isPresent()) {
            problemLogRepository.deleteById(requestedId);
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<ProblemLog>(responseHeaders, HttpStatus.NO_CONTENT);
        } else {
            throw new ProblemLogNotFoundException(requestedId);
        }
    }
}
