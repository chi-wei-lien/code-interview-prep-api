package example.CodeInterviewPrepAPI.Advices;

import example.CodeInterviewPrepAPI.Exceptions.ProblemLogNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProblemLogNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ProblemLogNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String CashCardNotFoundHandler(ProblemLogNotFoundException ex) {
        return ex.getMessage();
    }
}
