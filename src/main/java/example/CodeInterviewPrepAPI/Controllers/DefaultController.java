package example.CodeInterviewPrepAPI.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @GetMapping("/secure")
	public String index() {
		return "Welcome to the code interview prep api :>";
	}
}
