package example.CodeInterviewPrepAPI;

import example.CodeInterviewPrepAPI.Models.ProblemLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ProblemLogJsonTest {
    @Autowired
    private JacksonTester<ProblemLog> json;

    @Test
    public void cashCardSerializationTest() throws IOException {
        OffsetDateTime timestamp = OffsetDateTime.of(2007, 12, 3, 10, 15, 30, 0, ZoneOffset.of("+01:00"));
        ProblemLog problemLog = new ProblemLog(1111L, "2 sum", 1.2, "https://leetcode.com/problems/two-sum/", timestamp);
        assertThat(json.write(problemLog)).isStrictlyEqualToJson("ProblemLogJsonExpected.json");
        assertThat(json.write(problemLog)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(problemLog)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(1111);
        assertThat(json.write(problemLog)).hasJsonPathStringValue("@.name");
        assertThat(json.write(problemLog)).extractingJsonPathStringValue("@.name")
                .isEqualTo("2 sum");
        assertThat(json.write(problemLog)).hasJsonPathNumberValue("@.difficulty");
        assertThat(json.write(problemLog)).extractingJsonPathNumberValue("@.difficulty")
                .isEqualTo(1.2);
        assertThat(json.write(problemLog)).hasJsonPathStringValue("@.url");
        assertThat(json.write(problemLog)).extractingJsonPathStringValue("@.url")
                .isEqualTo("https://leetcode.com/problems/two-sum/");
        assertThat(json.write(problemLog)).hasJsonPathStringValue("@.timestamp");
        assertThat(json.write(problemLog)).extractingJsonPathStringValue("@.timestamp")
                .isEqualTo("2007-12-03T10:15:30+01:00");
    }

    @Test
    public void cashCardDeserializationTest() throws IOException {
        String expected = """
                {
                    "id":1111,
                    "name":"2 sum",
                    "difficulty":1.2,
                    "url":"https://leetcode.com/problems/two-sum/",
                    "timestamp":"2007-12-03T10:15:30+01:00"
                }
                """;
        OffsetDateTime timestamp = OffsetDateTime.of(2007, 12, 3, 10, 15, 30, 0, ZoneOffset.of("+01:00"));
        assertThat(json.parse(expected))
                .isEqualTo(new ProblemLog(1111L, "2 sum", 1.2, "https://leetcode.com/problems/two-sum/", timestamp));
        assertThat(json.parseObject(expected).getId()).isEqualTo(1111);
        assertThat(json.parseObject(expected).getName()).isEqualTo("2 sum");
        assertThat(json.parseObject(expected).getDifficulty()).isEqualTo(1.2);
        assertThat(json.parseObject(expected).getUrl()).isEqualTo("https://leetcode.com/problems/two-sum/");
        assertThat(json.parseObject(expected).getTimestamp()).isEqualTo("2007-12-03T10:15:30+01:00");
    }
}
