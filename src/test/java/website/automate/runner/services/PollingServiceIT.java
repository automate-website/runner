package website.automate.runner.services;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.apache.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import website.automate.runner.configs.RunnerSettings;

import java.text.MessageFormat;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PollingServiceIT {

    @ClassRule
    public static final WireMockClassRule wireMockRule = new WireMockClassRule(8081);

    @Autowired
    private PollingService pollingService;

    @Autowired
    private RunnerSettings runnerSettings;

    @Test
    public void jobsAreProvided() {
        String url = MessageFormat.format("/runners/{0}/jobs/next", runnerSettings.getId());
        wireMockRule.stubFor(post(urlEqualTo(url)).willReturn(aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("runners-job-next-post.json")));

        pollingService.pollJobs();

        wireMockRule.verify(postRequestedFor(urlEqualTo(url)));
    }
}