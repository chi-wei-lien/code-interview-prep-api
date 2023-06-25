package example.CodeInterviewPrepAPI;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import com.jayway.jsonpath.DocumentContext;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;

import static org.assertj.core.api.Assertions.assertThat;


@Sql(scripts = {"/drop_user_schema.sql", "/create_user_schema.sql"})
@SqlMergeMode(MergeMode.MERGE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthenticationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"/drop_user_schema.sql", "/create_user_schema.sql"})
    void shouldNotRegsiterUserWithLackOfData() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userNoEmailJsonObject = new JSONObject();
        userNoEmailJsonObject.put("username", "willy");
        userNoEmailJsonObject.put("password", "1234Willy@");
        HttpEntity<String> request = new HttpEntity<String>(userNoEmailJsonObject.toString(),
                headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signup",
                request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("No email is provided");

        JSONObject userNoUsernameJsonObject = new JSONObject();
        userNoUsernameJsonObject.put("password", "1234Willy@");
        userNoUsernameJsonObject.put("email", "willy@gmail.com");
        request = new HttpEntity<String>(userNoUsernameJsonObject.toString(), headers);
        response = restTemplate.postForEntity("/auth/signup", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        documentContext = JsonPath.parse(response.getBody());
        message = documentContext.read("$.message");
        assertThat(message).isEqualTo("No username is provided");

        JSONObject userNoPasswordJsonObject = new JSONObject();
        userNoPasswordJsonObject.put("username", "willy");
        userNoPasswordJsonObject.put("email", "willy@gmail.com");
        request = new HttpEntity<String>(userNoPasswordJsonObject.toString(), headers);
        response = restTemplate.postForEntity("/auth/signup", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        documentContext = JsonPath.parse(response.getBody());
        message = documentContext.read("$.message");
        assertThat(message).isEqualTo("No password is provided");
    }

    @Test
    @Sql(scripts = {"/drop_user_schema.sql", "/create_user_schema.sql"})
    @Sql(scripts = "/insert_user_data.sql")
    void shouldNotRegisterUserWithUsedUsername() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userUsedUsernameJsonObject = new JSONObject();
        userUsedUsernameJsonObject.put("username", "willy3124");
        userUsedUsernameJsonObject.put("email", "willy@gmail.com");
        userUsedUsernameJsonObject.put("password", "1234Willy@");
        HttpEntity<String> request = new HttpEntity<String>(userUsedUsernameJsonObject.toString(),
                headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signup",
                request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Username willy3124 is already taken!");
    }

    @Test
    @Sql(scripts = {"/drop_user_schema.sql", "/create_user_schema.sql"})
    @Sql(scripts = "/insert_user_data.sql")
    void shouldNotRegisterUserWithUsedEmail() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userUsedEmailJsonObject = new JSONObject();
        userUsedEmailJsonObject.put("username", "willy1111");
        userUsedEmailJsonObject.put("email", "cool@gmail.com");
        userUsedEmailJsonObject.put("password", "1234Willy@");
        HttpEntity<String> request = new HttpEntity<String>(userUsedEmailJsonObject.toString(),
                headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signup",
                request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Email cool@gmail.com is already taken!");
    }

    @Test
    @Sql(scripts = {"/drop_user_schema.sql", "/create_user_schema.sql"})
    void shouldNotRegisterUserWithWeakPassword() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        /* Test length */
        JSONObject userShortPasswordJsonObject = new JSONObject();
        userShortPasswordJsonObject.put("username", "willy1111");
        userShortPasswordJsonObject.put("email", "hi@gmail.com");
        userShortPasswordJsonObject.put("password", "1@34W67");
        HttpEntity<String> request = new HttpEntity<String>(userShortPasswordJsonObject.toString(),
                headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signup",
                request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Invalid password format");

        /* Test special char */
        JSONObject userNoSpecialCharJsonObject = new JSONObject();
        userNoSpecialCharJsonObject.put("username", "willy1111");
        userNoSpecialCharJsonObject.put("email", "hi@gmail.com");
        userNoSpecialCharJsonObject.put("password", "1234W679");
        request = new HttpEntity<String>(userNoSpecialCharJsonObject.toString(), headers);
        response = restTemplate.postForEntity("/auth/signup", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        documentContext = JsonPath.parse(response.getBody());
        message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Invalid password format");

        /* Test uppercase letter */
        JSONObject userNoUpperCharJsonObject = new JSONObject();
        userNoUpperCharJsonObject.put("username", "willy1111");
        userNoUpperCharJsonObject.put("email", "hi@gmail.com");
        userNoUpperCharJsonObject.put("password", "1@34w678");
        request = new HttpEntity<String>(userNoUpperCharJsonObject.toString(), headers);
        response = restTemplate.postForEntity("/auth/signup", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        documentContext = JsonPath.parse(response.getBody());
        message = documentContext.read("$.message");
        assertThat(message).isEqualTo("Invalid password format");
    }

    @Test
    @Sql(scripts = {"/drop_user_schema.sql", "/create_user_schema.sql"})
    void shouldRegisterUserWithNormalPassword() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject userNormalJsonObject = new JSONObject();
        userNormalJsonObject.put("username", "willy1111");
        userNormalJsonObject.put("email", "hi@gmail.com");
        userNormalJsonObject.put("password", "1@34W678");
        HttpEntity<String> request = new HttpEntity<>(userNormalJsonObject.toString(),
                headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signup",
                request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String username = documentContext.read("$.username");
        assertThat(username).isEqualTo("willy1111");
        String email = documentContext.read("$.email");
        assertThat(email).isEqualTo("hi@gmail.com");
    }

    @Test
    @Sql(scripts = {"/drop_user_schema.sql", "/create_user_schema.sql"})
    void shouldSignInRegisteredUser() throws JSONException {
        /* Sign up */
        HttpHeaders singUpHeaders = new HttpHeaders();
        singUpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject userNormalJsonObject = new JSONObject();
        userNormalJsonObject.put("username", "willy3124");
        userNormalJsonObject.put("email", "hi@gmail.com");
        userNormalJsonObject.put("password", "cool3124Willy%");
        HttpEntity<String> signUpRequest = new HttpEntity<>(userNormalJsonObject.toString(),
                singUpHeaders);
        restTemplate.postForEntity("/auth/signup", signUpRequest, String.class);

        /* Sign in */
        HttpHeaders signInHeaders = new HttpHeaders();
        signInHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject credentialJsonObject = new JSONObject();
        credentialJsonObject.put("username", "willy3124");
        credentialJsonObject.put("password", "cool3124Willy%");
        HttpEntity<String> request = new HttpEntity<>(credentialJsonObject.toString(),
                signInHeaders);
        ResponseEntity<String> signInResponse = restTemplate.postForEntity("/auth/signin",
                request, String.class);
        assertThat(signInResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(signInResponse.getBody());
        String username = documentContext.read("$.username");
        assertThat(username).isEqualTo("willy3124");
        String email = documentContext.read("$.email");
        assertThat(email).isEqualTo("hi@gmail.com");
    }

    @Test
    @Sql(scripts = {"/drop_user_schema.sql", "/create_user_schema.sql"})
    void shouldNotSignInWithWrongCredentials() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        /* Sign up */
        JSONObject signUpJsonObject = new JSONObject();
        signUpJsonObject.put("username", "willy3124");
        signUpJsonObject.put("email", "hi@gmail.com");
        signUpJsonObject.put("password", "cool3124Willy%");
        HttpEntity<String> signUpRequest = new HttpEntity<>(signUpJsonObject.toString(),
                headers);
        restTemplate.postForEntity("/auth/signup", signUpRequest, String.class);

        /* Sign in (1) */
        JSONObject signInJsonObject = new JSONObject();
        signInJsonObject.put("username", "willy3125");
        signInJsonObject.put("password", "cool3124Willy%");
        HttpEntity<String> request1 = new HttpEntity<>(signInJsonObject.toString(),
                headers);
        ResponseEntity<String> signInResponse1 = restTemplate.postForEntity("/auth/signin",
                request1, String.class);
        assertThat(signInResponse1.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        DocumentContext documentContext1 = JsonPath.parse(signInResponse1.getBody());
        String message1 = documentContext1.read("$.message");
        assertThat(message1).isEqualTo("Bad credentials");

        /* Sign in (2) */
        signInJsonObject = new JSONObject();
        signInJsonObject.put("username", "willy3124");
        signInJsonObject.put("password", "Cool3124willy%");
        HttpEntity<String> request2 = new HttpEntity<>(signInJsonObject.toString(),
                headers);
        ResponseEntity<String> signInResponse2 = restTemplate.postForEntity("/auth/signin",
                request2, String.class);
        assertThat(signInResponse2.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        DocumentContext documentContext2 = JsonPath.parse(signInResponse2.getBody());
        String message2 = documentContext2.read("$.message");
        assertThat(message2).isEqualTo("Bad credentials");
    }
}
