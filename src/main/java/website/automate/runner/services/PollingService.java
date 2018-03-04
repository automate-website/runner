package website.automate.runner.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import website.automate.runner.configs.RunnerSettings;

import java.util.List;

import static java.text.MessageFormat.format;

@Service
public class PollingService {
    private static final Logger LOG = LoggerFactory.getLogger(PollingService.class);
    private static final String RUNNER_NEXT_JOBS_URI_TEMPLATE = "/runners/{0}/jobs/next";

    @Autowired
    private RunnerSettings runnerSettings;

    @Autowired
    private RestTemplate restTemplate;

    private ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<List<String>>() { };

    @Scheduled(fixedDelayString = "${website.automate.job-poll-delay:5000}")
    public void pollJobs() {
        try {
            ResponseEntity<List<String>> jobsResponse = restTemplate.exchange(
                    format(RUNNER_NEXT_JOBS_URI_TEMPLATE, runnerSettings.getId()),
                    HttpMethod.POST,
                    null,
                    parameterizedTypeReference
            );
            LOG.info("Got jobs: {}", jobsResponse.getBody());
        } catch (Exception e) {
            LOG.error("Connection error: {}!", e.getMessage());
        }
    }
}