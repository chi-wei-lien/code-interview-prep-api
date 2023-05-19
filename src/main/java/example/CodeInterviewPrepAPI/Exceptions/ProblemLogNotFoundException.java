package example.CodeInterviewPrepAPI;

public class ProblemLogNotFoundException extends RuntimeException{
    ProblemLogNotFoundException(Long requestedId) {
        super(String.format("Could not find problem log %d", requestedId));
    }
}
