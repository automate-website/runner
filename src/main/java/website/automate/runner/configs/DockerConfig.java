package website.automate.runner.configs;

public class DockerConfig {
    private String uri;
    private Integer stopTimeout;
    private String image;
    private String containerLabel;
    private Integer maxExecutionTime;
    private String command;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getStopTimeout() {
        return stopTimeout;
    }

    public void setStopTimeout(Integer stopTimeout) {
        this.stopTimeout = stopTimeout;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContainerLabel() {
        return containerLabel;
    }

    public void setContainerLabel(String containerLabel) {
        this.containerLabel = containerLabel;
    }

    public Integer getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(Integer maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
