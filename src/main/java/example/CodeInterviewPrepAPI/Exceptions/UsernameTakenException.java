package example.CodeInterviewPrepAPI.Exceptions;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String username) {
        super(String.format("Username %s is already taken!", username));
    }
}
