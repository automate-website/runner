package website.automate.runner.docker.services;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.automate.runner.configs.RunnerSettings;
import website.automate.runner.docker.mappers.JobRunConfigDockerEnvMapper;
import website.automate.runner.models.JobRunConfig;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DockerService {
    private static final Logger LOG = LoggerFactory.getLogger(DockerService.class);

    @Autowired
    private RunnerSettings runnerSettings;

    @Autowired
    private DockerClient docker;

    @Autowired
    private JobRunConfigDockerEnvMapper jobRunConfigDockerEnvMapper;


    public List<Container> getContainers(boolean showAll) throws DockerException, InterruptedException {
        String containerLabel = runnerSettings.getDocker().getContainerLabel();
        DockerClient.ListContainersParam listContainersParam = DockerClient.ListContainersParam.withLabel(containerLabel);
        List<Container> containers = docker.listContainers(listContainersParam, DockerClient.ListContainersParam.allContainers(showAll));

        return containers;
    }

    public void removeStale() throws DockerException, InterruptedException {
        List<Container> containers = getContainers(false);
        Instant latestCreationTime = Instant.now().minusSeconds(runnerSettings.getDocker().getMaxExecutionTime());

        for (Container container : containers) {
            String containerId = container.id();
            try {
                ContainerInfo containerInfo = docker.inspectContainer(containerId);
                boolean isStale = containerInfo.created().toInstant().isBefore(latestCreationTime);
                if (isStale) {
                    stopContainer(containerId);
                }
            } catch (Exception e) {
                LOG.warn("Could not stop container#{}: ", containerId, e.getMessage());
            }
        }
    }

    public void runJob(JobRunConfig jobRunConfig) throws DockerException, InterruptedException {
        String imageName = runnerSettings.getDocker().getImage();

        pullImageIfNotPresent(imageName);

        ContainerConfig containerConfig = ContainerConfig.builder()
                .image(imageName)
                .cmd(runnerSettings.getDocker().getCommand())
                .env(jobRunConfigDockerEnvMapper.toEnv(jobRunConfig))
                .labels(getLabels(jobRunConfig))
                .build();

        ContainerCreation creation = docker.createContainer(containerConfig);
        String id = creation.id();

        docker.startContainer(id);
    }

    private void pullImageIfNotPresent(String imageName) throws DockerException, InterruptedException {
        List<Image> images = docker.listImages(DockerClient.ListImagesParam.byName(imageName));
        if (images.isEmpty()) {
            docker.pull(imageName);
        }
    }

    public void stopContainer(String containerId) throws DockerException, InterruptedException {
        LOG.debug("Stopping container#{}.", containerId);
        docker.stopContainer(containerId, runnerSettings.getDocker().getStopTimeout());
    }

    private Map<String, String> getLabels(JobRunConfig jobRunConfig) {
        Map<String, String> labels = new HashMap<>();
        labels.put(runnerSettings.getDocker().getContainerLabel(), jobRunConfig.getJobId());
        return labels;
    }
}
