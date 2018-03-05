package website.automate.runner.services;

import com.spotify.docker.client.exceptions.DockerException;
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
import website.automate.runner.models.JobRunConfig;

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

    @Autowired
    private JobExecutorService jobExecutorService;

    private ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<List<JobRunConfig>>() { };

    @Scheduled(fixedDelayString = "${website.automate.runner.clean-up-delay:60000}")
    public void cleanStaleJobs() throws DockerException, InterruptedException {
        jobExecutorService.cleanStaleJobs();
    }

    @Scheduled(fixedDelayString = "${website.automate.runner.job-poll-delay:5000}")
    public void pollJobs() {
        try {
            int jobCount = jobExecutorService.countJobs();
            if (jobCount >= runnerSettings.getConcurrent()) {
                LOG.info("Currently busy with {} of {} possible jobs. Skipping...", jobCount, runnerSettings.getConcurrent());
                return;
            }

            ResponseEntity<List<JobRunConfig>> jobsResponse = restTemplate.exchange(
                    format(RUNNER_NEXT_JOBS_URI_TEMPLATE, runnerSettings.getId()),
                    HttpMethod.POST,
                    null,
                    parameterizedTypeReference
            );

            List<JobRunConfig> jobRunConfigs = jobsResponse.getBody();
            LOG.info("Got jobs: {}", jobRunConfigs);

            jobRunConfigs.forEach(jobRunConfig -> {
                try {
                    jobExecutorService.executeJob(jobRunConfig);
                } catch (DockerException | InterruptedException e) {
                    LOG.error("Error on job execution!", e);
                }
            });

        } catch (Exception e) {
            LOG.error("Connection error: {}!", e.getMessage());
        }
    }
}