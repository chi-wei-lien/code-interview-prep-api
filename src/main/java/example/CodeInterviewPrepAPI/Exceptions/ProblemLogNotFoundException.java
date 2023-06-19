package example.CodeInterviewPrepAPI.Exceptions;

public class ProblemLogNotFoundException extends RuntimeException{
    public ProblemLogNotFoundException(Long requestedId) {
        super(String.format("Could not find problem log %d", requestedId));
    }
}
