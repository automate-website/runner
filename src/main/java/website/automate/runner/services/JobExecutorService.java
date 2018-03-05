package website.automate.runner.services;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.automate.runner.docker.services.DockerService;
import website.automate.runner.models.JobRunConfig;

import java.util.List;

@Service
public class JobExecutorService {

    @Autowired
    private DockerService dockerService;

    public int countJobs() throws DockerException, InterruptedException {
        List<Container> containers = dockerService.getContainers(false);
        return containers.size();
    }

    public void executeJob(JobRunConfig jobRunConfig) throws DockerException, InterruptedException {
        dockerService.runJob(jobRunConfig);
    }

    public void cleanStaleJobs() throws DockerException, InterruptedException {
        dockerService.removeStale();
    }
}
