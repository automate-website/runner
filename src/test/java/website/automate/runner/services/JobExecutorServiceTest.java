package website.automate.runner.services;

import com.spotify.docker.client.exceptions.DockerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import website.automate.runner.docker.services.DockerService;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JobExecutorServiceTest {

    @InjectMocks
    private JobExecutorService jobExecutorService;

    @Mock
    private DockerService dockerService;

    @Test
    public void staleJobsAreCleaned() throws DockerException, InterruptedException {
        jobExecutorService.cleanStaleJobs();

        verify(dockerService).removeStale();
    }
}