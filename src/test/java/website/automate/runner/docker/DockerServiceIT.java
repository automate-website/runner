package website.automate.runner.docker;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import website.automate.runner.docker.services.DockerService;
import website.automate.runner.models.JobRunConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Ignore
public class DockerServiceIT {

    @Autowired
    private DockerService dockerService;

    @Before
    public void cleanUp() throws DockerException, InterruptedException {
        List<Container> containers = dockerService.getContainers(false);

        containers.forEach(c -> {
            try {
                dockerService.stopContainer(c.id());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void numberOfContainersIsProvided() throws DockerException, InterruptedException {
        List<Container> containers = dockerService.getContainers(false);

        assertThat(containers, hasSize(0));
    }

    @Test
    public void containerShouldStartAndStop() throws DockerException, InterruptedException {
        JobRunConfig jobRunConfig = createJobRunConfig();

        dockerService.runJob(jobRunConfig);

        List<Container> containers = dockerService.getContainers(false);
        assertThat(containers, hasSize(1));

        dockerService.stopContainer(containers.get(0).id());

        List<Container> containersAfterStop = dockerService.getContainers(false);
        assertThat(containersAfterStop, hasSize(0));
    }

    @Test
    public void staleContainersAreRemoved() throws DockerException, InterruptedException {
        JobRunConfig jobRunConfig = createJobRunConfig();
        dockerService.runJob(jobRunConfig);

        List<Container> containers = dockerService.getContainers(false);
        assertThat(containers, hasSize(1));

        TimeUnit.SECONDS.sleep(2);

        dockerService.removeStale();

        List<Container> containersAfterStop = dockerService.getContainers(false);
        assertThat(containersAfterStop, hasSize(0));
    }

    private JobRunConfig createJobRunConfig() {
        JobRunConfig jobRunConfig = new JobRunConfig();
        jobRunConfig.setTakeScreenshots(JobRunConfig.TakeScreenshot.ON_EVERY_STEP);
        jobRunConfig.setPullUrl("http://localhost:8082/123/scenario");
        jobRunConfig.setPushUrl("http://localhost:8082/123/report");
        jobRunConfig.setDisplayGeometry("1280x768x24");
        jobRunConfig.setScenarioPattern("*");
        jobRunConfig.setContext(null);
        return jobRunConfig;
    }
}