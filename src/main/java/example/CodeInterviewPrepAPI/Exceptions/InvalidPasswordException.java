package example.CodeInterviewPrepAPI.Exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String msg) {
        super(msg);
    }
}
