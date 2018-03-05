package website.automate.runner.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.spotify.docker.client.exceptions.DockerException;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import website.automate.runner.configs.RunnerSettings;
import website.automate.runner.models.JobRunConfig;

import java.text.MessageFormat;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PollingServiceIT {

    @ClassRule
    public static final WireMockClassRule wireMockRule = new WireMockClassRule(8081);
    public static final String DISPLAY_GEOMETRY = "1024*768*16";

    @Autowired
    private PollingService pollingService;

    @Autowired
    private RunnerSettings runnerSettings;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JobExecutorService jobExecutorService;

    @Before
    public void resetWireMock() {
        wireMockRule.resetAll();
    }

    @Test
    public void jobsAreProvidedAndStarted() throws DockerException, InterruptedException, JsonProcessingException {
        JobRunConfig jobRunConfig = createJobRunConfig();

        String url = MessageFormat.format("/runners/{0}/jobs/next", runnerSettings.getId());
        wireMockRule.stubFor(post(urlEqualTo(url)).willReturn(aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(asList(jobRunConfig)))));

        pollingService.pollJobs();

        wireMockRule.verify(postRequestedFor(urlEqualTo(url)));

        Mockito.verify(jobExecutorService).countJobs();
        Mockito.verify(jobExecutorService).executeJob(jobRunConfig);
    }

    private JobRunConfig createJobRunConfig() {
        JobRunConfig jobRunConfig = new JobRunConfig();
        jobRunConfig.setJobId(randomString());
        jobRunConfig.setPullUrl(randomString());
        jobRunConfig.setPushUrl(randomString());
        jobRunConfig.setDisplayGeometry(DISPLAY_GEOMETRY);

        return jobRunConfig;
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void skipPollingIfConcurrentJobsLimitIsExceeded() throws DockerException, InterruptedException {
        when(jobExecutorService.countJobs()).thenReturn(100);

        pollingService.pollJobs();
    }
}