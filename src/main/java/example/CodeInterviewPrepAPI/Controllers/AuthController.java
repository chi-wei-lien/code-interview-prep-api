/* Learned from https://www.bezkoder.com/spring-boot-security-jwt/#Create_JWT_Utility_class */
package example.CodeInterviewPrepAPI.Controllers;

import example.CodeInterviewPrepAPI.Exceptions.EmailTakenException;
import example.CodeInterviewPrepAPI.Exceptions.UsernameTakenException;
import example.CodeInterviewPrepAPI.Models.User;
import example.CodeInterviewPrepAPI.Payload.Request.LoginRequest;
import example.CodeInterviewPrepAPI.Payload.Request.SignupRequest;
import example.CodeInterviewPrepAPI.Payload.Response.MessageResponse;
import example.CodeInterviewPrepAPI.Payload.Response.UserInfoResponse;
import example.CodeInterviewPrepAPI.Repositories.UserRepository;
import example.CodeInterviewPrepAPI.Security.JwtUtils;
import example.CodeInterviewPrepAPI.Security.PasswordValidator;
import example.CodeInterviewPrepAPI.Security.UserDetailsImpl;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                                          userDetails.getUsername(),
                                          userDetails.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        if (username == null) {
            return ResponseEntity.badRequest().body(new ConstraintViolationException("No username is provided", null));
        }
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new UsernameTakenException(username));
        }

        String email = signUpRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new EmailTakenException(email));
        }
        if (email == null) {
            return ResponseEntity.badRequest().body(new ConstraintViolationException("No email is provided", null));
        }

        // Create new user's account
        String password = signUpRequest.getPassword();
        if (password == null) {
            return ResponseEntity.badRequest().body(new ConstraintViolationException("No password is provided", null));
        }

        PasswordValidator.validate(password);

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(password));

        userRepository.save(user);

        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(new UserInfoResponse(user.getId(),
                user.getUsername(),
                user.getEmail()), responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}