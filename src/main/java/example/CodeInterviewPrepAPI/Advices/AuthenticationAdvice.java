package example.CodeInterviewPrepAPI.Advices;

import example.CodeInterviewPrepAPI.Exceptions.EmailTakenException;
import example.CodeInterviewPrepAPI.Exceptions.InvalidPasswordException;
import example.CodeInterviewPrepAPI.Exceptions.UsernameTakenException;
import example.CodeInterviewPrepAPI.Payload.Response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class AuthenticationAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    MessageResponse InvalidPasswordHandler(InvalidPasswordException ex) {
        return new MessageResponse(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    MessageResponse ConstraintViolationHandler(ConstraintViolationException ex) {
//        TODO: better handle this
        return new MessageResponse("Constraint violation");
    }

    @ResponseBody
    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    MessageResponse UsernameTakenHandler(UsernameTakenException ex) {
        return new MessageResponse(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    MessageResponse EmailTakenHandler(EmailTakenException ex) {
        return new MessageResponse(ex.getMessage());
    }
}
