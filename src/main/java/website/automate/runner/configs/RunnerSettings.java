package website.automate.runner.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("website.automate.runner")
@Configuration
public class RunnerSettings {
    private String id;
    private String apiUrl;
    private Integer concurrent;

    private DockerConfig docker;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }

    public DockerConfig getDocker() {
        return docker;
    }

    public void setDocker(DockerConfig docker) {
        this.docker = docker;
    }
}
