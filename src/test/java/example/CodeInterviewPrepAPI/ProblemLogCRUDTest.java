package example.CodeInterviewPrepAPI;

import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import com.jayway.jsonpath.DocumentContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@Sql({ "/drop_problemlog_schema.sql", "/create_problemlog_schema.sql" })
@Sql({"/drop_user_schema.sql", "/create_user_schema.sql", "/insert_user_data.sql"})
@SqlMergeMode(MergeMode.MERGE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class ProblemLogCRUDTest {
    @Autowired
    TestRestTemplate restTemplate;
//    List<String> cookies;

//    @Before
//    public void login() throws JSONException  {
//        JSONObject userNoEmailJsonObject = new JSONObject();
//        userNoEmailJsonObject.put("username", "willy");
//        userNoEmailJsonObject.put("password", "1234Willy@");
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<String> requestBody = new HttpEntity<String>(userNoEmailJsonObject.toString(), headers);
//        cookies = restTemplate.postForEntity("/auth/signin", requestBody, String.class)
//                .getHeaders()
//                .get("Set-Cookie");
//    }
 
    @Test
    @Sql({ "/drop_problemlog_schema.sql", "/create_problemlog_schema.sql", "/insert_problemlog_data.sql" })
    void shouldReturnProblemLogWithKnownId() throws JSONException {
        JSONObject credentialObject = new JSONObject();
        credentialObject.put("username", "willy3124");
        credentialObject.put("password", "cool3124Willy%");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestBody = new HttpEntity<String>(credentialObject.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth/signin", requestBody, String.class);
        System.out.println("response");
        System.out.println(responseEntity);

        headers = new HttpHeaders();
//        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        Map<String, String> params = new HashMap<>();
        ResponseEntity<String> response = restTemplate.exchange(
                "/problemlog/10", HttpMethod.GET, requestEntity, String.class, params);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(10);
		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("3 sum");
        Double difficulty = documentContext.read("$.difficulty");
		assertThat(difficulty).isEqualTo(3.7);
        String url = documentContext.read("$.url");
		assertThat(url).isEqualTo("https://leetcode.com/problems/3sum/");
        OffsetDateTime timestamp = OffsetDateTime.parse(documentContext.read("$.timestamp"));
        OffsetDateTime expectedTimestamp = OffsetDateTime.of(2004, 10, 19, 10, 23, 54, 0, ZoneOffset.of("+02:00"));
		assertThat(timestamp).isEqualTo(expectedTimestamp);
    }

//    @Test
//	@Sql({ "/drop_problemlog_schema.sql", "/create_problemlog_schema.sql" })
//	void shouldNotReturnProblemLogWithUnknownId() {
//		ResponseEntity<String> response = restTemplate.getForEntity("/problemlog/10", String.class);
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//		assertThat(response.getBody()).isEqualTo(String.format("Could not find problem log %d", 10));
//	}
//
//    @Test
//	void shouldCreateAndReturnProblemLog() throws JSONException {
//        OffsetDateTime expectedTimestamp = OffsetDateTime.of(2023, 4, 10, 10, 23, 54, 0, ZoneOffset.of("+02:00"));
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		JSONObject problemLogJsonObject = new JSONObject();
//		problemLogJsonObject.put("name", "4 sum");
//		problemLogJsonObject.put("difficulty", "4.0");
//		problemLogJsonObject.put("url", "https://leetcode.com/problems/4sum/");
//		problemLogJsonObject.put("timestamp", expectedTimestamp.toString());
//		HttpEntity<String> request = new HttpEntity<String>(problemLogJsonObject.toString(), headers);
//
//		ResponseEntity<String> response = restTemplate.postForEntity("/problemlog", request, String.class);
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//		DocumentContext documentContext = JsonPath.parse(response.getBody());
//		String name = documentContext.read("$.name");
//		assertThat(name).isEqualTo("4 sum");
//        Double difficulty = documentContext.read("$.difficulty");
//		assertThat(difficulty).isEqualTo(4.0);
//        String url = documentContext.read("$.url");
//		assertThat(url).isEqualTo("https://leetcode.com/problems/4sum/");
//        OffsetDateTime timestamp = OffsetDateTime.parse(documentContext.read("$.timestamp"));
//        assertThat(timestamp).isEqualTo(expectedTimestamp);
//	}
//
//    @Test
//    @Sql({ "/insert_problemlog_data.sql" })
//    void shouldReturnPageOfProblemLogWithRightSizeSortedWithTimestamp() {
//        ResponseEntity<String> response = restTemplate.getForEntity("/problemlog?page=1&size=10", String.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//		DocumentContext documentContext = JsonPath.parse(response.getBody());
//		List<Number> ids = documentContext.read("*.id");
//        assertThat(ids.size()).isEqualTo(10);
//		List<String> timestampsAsStrings = documentContext.read("*.timestamp");
//        OffsetDateTime prevTimestamp = OffsetDateTime.parse(timestampsAsStrings.get(0));
//
//        for (int i = 1; i < timestampsAsStrings.size(); ++i) {
//            OffsetDateTime currTimestamp = OffsetDateTime.parse(timestampsAsStrings.get(i));
//            assertThat(prevTimestamp.compareTo(currTimestamp)).isEqualTo(1);
//            prevTimestamp = currTimestamp;
//        }
//    }
//
//	@Test
//	@Sql(scripts = {"/drop_problemlog_schema.sql", "/create_problemlog_schema.sql"},
//			statements="INSERT INTO PROBLEM_LOG (ID, NAME, DIFFICULTY, URL, TIMESTAMP) VALUES (10, '3 sum', 3.7, 'https://leetcode.com/problems/3sum/', '2004-10-19 10:23:54+02');")
//    void shouldUpdateProblemLog() throws JSONException {
//		OffsetDateTime expectedTimestamp = OffsetDateTime.of(2023, 4, 10, 10, 23, 54, 0, ZoneOffset.of("+02:00"));
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		JSONObject problemLogJsonObject = new JSONObject();
//		problemLogJsonObject.put("name", "4 sum");
//		problemLogJsonObject.put("difficulty", "4.0");
//		problemLogJsonObject.put("url", "https://leetcode.com/problems/4sum/");
//		problemLogJsonObject.put("timestamp", expectedTimestamp.toString());
//		HttpEntity<String> request = new HttpEntity<String>(problemLogJsonObject.toString(), headers);
//
//        ResponseEntity<String> putResponse = restTemplate.exchange("/problemlog/10", HttpMethod.PUT, request, String.class);
//        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//
//		ResponseEntity<String> getResponse = restTemplate.getForEntity("/problemlog/10", String.class);
//        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
//
//		Number id = documentContext.read("$.id");
//		assertThat(id).isEqualTo(10);
//		String name = documentContext.read("$.name");
//		assertThat(name).isEqualTo("4 sum");
//        Double difficulty = documentContext.read("$.difficulty");
//		assertThat(difficulty).isEqualTo(4.0);
//        String url = documentContext.read("$.url");
//		assertThat(url).isEqualTo("https://leetcode.com/problems/4sum/");
//        OffsetDateTime timestamp = OffsetDateTime.parse(documentContext.read("$.timestamp"));
//        assertThat(timestamp).isEqualTo(expectedTimestamp);
//    }
//
//	@Test
//	@Sql({ "/drop_problemlog_schema.sql", "/create_problemlog_schema.sql" })
//	void shouldCreateProblemLogWhenUpdateWithUnknownId() throws JSONException {
//		OffsetDateTime expectedTimestamp = OffsetDateTime.of(2023, 4, 10, 10, 23, 54, 0, ZoneOffset.of("+02:00"));
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		JSONObject problemLogJsonObject = new JSONObject();
//		problemLogJsonObject.put("name", "4 sum");
//		problemLogJsonObject.put("difficulty", "4.0");
//		problemLogJsonObject.put("url", "https://leetcode.com/problems/4sum/");
//		problemLogJsonObject.put("timestamp", expectedTimestamp.toString());
//		HttpEntity<String> request = new HttpEntity<String>(problemLogJsonObject.toString(), headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("/problemlog/10", HttpMethod.PUT, request, String.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//		DocumentContext documentContext = JsonPath.parse(response.getBody());
//		String name = documentContext.read("$.name");
//		assertThat(name).isEqualTo("4 sum");
//        Double difficulty = documentContext.read("$.difficulty");
//		assertThat(difficulty).isEqualTo(4.0);
//        String url = documentContext.read("$.url");
//		assertThat(url).isEqualTo("https://leetcode.com/problems/4sum/");
//        OffsetDateTime timestamp = OffsetDateTime.parse(documentContext.read("$.timestamp"));
//        assertThat(timestamp).isEqualTo(expectedTimestamp);
//    }
//
//	@Test
//	@Sql(scripts = {"/drop_problemlog_schema.sql", "/create_problemlog_schema.sql"},
//			statements="INSERT INTO PROBLEM_LOG (ID, NAME, DIFFICULTY, URL, TIMESTAMP) VALUES (10, '3 sum', 3.7, 'https://leetcode.com/problems/3sum/', '2004-10-19 10:23:54+02');")
//	void shouldDeleteProblemLogWithKnownId() {
//		HttpHeaders headers = new HttpHeaders();
//		HttpEntity<String> request = new HttpEntity<String>("", headers);
//		ResponseEntity<String> deleteResponse = restTemplate.exchange("/problemlog/10", HttpMethod.DELETE, request, String.class);
//		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//
//		ResponseEntity<String> getResponse = restTemplate.getForEntity("/problemlog/10", String.class);
//		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//		assertThat(getResponse.getBody()).isEqualTo(String.format("Could not find problem log %d", 10));
//	}
//
//	@Test
//	@Sql({"/drop_problemlog_schema.sql", "/create_problemlog_schema.sql"})
//	void shouldNotDeleteProblemLogWithUnknownId() {
//		HttpHeaders headers = new HttpHeaders();
//		HttpEntity<String> request = new HttpEntity<String>("", headers);
//		ResponseEntity<String> deleteResponse = restTemplate.exchange("/problemlog/10", HttpMethod.DELETE, request, String.class);
//		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//	}
}
