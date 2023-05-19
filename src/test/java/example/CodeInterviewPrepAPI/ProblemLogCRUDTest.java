package example.CodeInterviewPrepAPI;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import com.jayway.jsonpath.DocumentContext;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.jdbc.SqlMergeMode.MergeMode;
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
    void shouldReturnProblemLogWithAKnownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/problemlog/10", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(10);

		Double name = documentContext.read("$.name");
		assertThat(name).isEqualTo("3 sum");
    }


}
