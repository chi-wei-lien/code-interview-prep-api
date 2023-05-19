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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@Sql({ "/drop_problemlog_schema.sql", "/create_problemlog_schema.sql" })
@SqlMergeMode(MergeMode.MERGE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class ProblemLogCRUDTest {
    @Autowired
    TestRestTemplate restTemplate;
 
    @Test
    @Sql(statements="INSERT INTO PROBLEM_LOG (ID, NAME, DIFFICULTY, URL, TIMESTAMP) VALUES (10, '3 sum', 3.7, 'https://leetcode.com/problems/3sum/', '2004-10-19 10:23:54+02');")
    void shouldReturnProblemLogWithKnownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/problemlog/10", String.class);

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

    @Test
	void shouldNotReturnProblemLogWithUnknownId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/problemlog/9999", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isEqualTo(String.format("Could not find problem log %d", 9999));
	}

    @Test
	void shouldCreateAndReturnProblemLog() throws JSONException {
        OffsetDateTime expectedTimestamp = OffsetDateTime.of(2023, 4, 10, 10, 23, 54, 0, ZoneOffset.of("+02:00"));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject cashCardJsonObject = new JSONObject();
		cashCardJsonObject.put("name", "4 sum");
		cashCardJsonObject.put("difficulty", "4.0");
		cashCardJsonObject.put("url", "https://leetcode.com/problems/4sum/");
		cashCardJsonObject.put("timestamp", expectedTimestamp.toString());
		HttpEntity<String> request = new HttpEntity<String>(cashCardJsonObject.toString(), headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/problemlog/new", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("4 sum");
        Double difficulty = documentContext.read("$.difficulty");
		assertThat(difficulty).isEqualTo(4.0);
        String url = documentContext.read("$.url");
		assertThat(url).isEqualTo("https://leetcode.com/problems/4sum/");

        OffsetDateTime timestamp = OffsetDateTime.parse(documentContext.read("$.timestamp"));
        assertThat(timestamp).isEqualTo(expectedTimestamp);
	}

    @Test
    @Sql({ "/insert_problemlog_data.sql" })
    void shouldReturnPageOfProblemLogWithRightSizeSortedWithTimestamp() {
        ResponseEntity<String> response = restTemplate.getForEntity("/problemlog?page=1&size=10", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext documentContext = JsonPath.parse(response.getBody());

		List<Number> ids = documentContext.read("*.id");
        assertThat(ids.size()).isEqualTo(10);
		List<String> timestampsAsStrings = documentContext.read("*.timestamp");
        OffsetDateTime prevTimestamp = OffsetDateTime.parse(timestampsAsStrings.get(0));

        for (int i = 1; i < timestampsAsStrings.size(); ++i) {
            OffsetDateTime currTimestamp = OffsetDateTime.parse(timestampsAsStrings.get(i));
            assertThat(prevTimestamp.compareTo(currTimestamp)).isEqualTo(1);
            prevTimestamp = currTimestamp;
        }
    }
}
