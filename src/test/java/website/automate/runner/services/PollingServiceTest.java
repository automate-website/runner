package website.automate.runner.services;

import com.spotify.docker.client.exceptions.DockerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PollingServiceTest {

    @InjectMocks
    private PollingService pollingService;

    @Mock
    private JobExecutorService jobExecutorService;

    @Test
    public void jobCleaningIsPerformed() throws DockerException, InterruptedException {
        pollingService.cleanStaleJobs();

        verify(jobExecutorService).cleanStaleJobs();
    }

}