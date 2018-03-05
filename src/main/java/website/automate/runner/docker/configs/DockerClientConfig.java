package website.automate.runner.docker.configs;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import website.automate.runner.configs.RunnerSettings;

@Configuration
public class DockerClientConfig {

    @Bean
    public DockerClient dockerClient(RunnerSettings runnerSettings) {
        return DefaultDockerClient.builder()
                .uri(runnerSettings.getDocker().getUri())
                .build();
    }
}
