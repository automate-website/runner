package website.automate.runner.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobRunConfig {
    private String jobId;
    private TakeScreenshot takeScreenshots;
    private String pullUrl;
    private String pushUrl;
    private String scenarioPattern;
    private String displayGeometry;
    private Map<String, String> context;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public TakeScreenshot getTakeScreenshots() {
        return takeScreenshots;
    }

    public void setTakeScreenshots(TakeScreenshot takeScreenshots) {
        this.takeScreenshots = takeScreenshots;
    }

    public String getPullUrl() {
        return pullUrl;
    }

    public void setPullUrl(String pullUrl) {
        this.pullUrl = pullUrl;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public String getScenarioPattern() {
        return scenarioPattern;
    }

    public void setScenarioPattern(String scenarioPattern) {
        this.scenarioPattern = scenarioPattern;
    }

    public String getDisplayGeometry() {
        return displayGeometry;
    }

    public void setDisplayGeometry(String displayGeometry) {
        this.displayGeometry = displayGeometry;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobRunConfig that = (JobRunConfig) o;
        return Objects.equals(jobId, that.jobId) &&
                takeScreenshots == that.takeScreenshots &&
                Objects.equals(pullUrl, that.pullUrl) &&
                Objects.equals(pushUrl, that.pushUrl) &&
                Objects.equals(scenarioPattern, that.scenarioPattern) &&
                Objects.equals(displayGeometry, that.displayGeometry) &&
                Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, takeScreenshots, pullUrl, pushUrl, scenarioPattern, displayGeometry, context);
    }

    public enum TakeScreenshot {
        NEVER,
        ON_FAILURE,
        ON_EVERY_STEP;
    }

}
