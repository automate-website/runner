package website.automate.runner.docker.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import website.automate.runner.configs.RunnerSettings;
import website.automate.runner.models.JobRunConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.text.MessageFormat.format;

@Component
public class JobRunConfigDockerEnvMapper {
    @Autowired
    private RunnerSettings runnerSettings;

    public List<String> toEnv(JobRunConfig jobRunConfig) {
        List<String> envList = new ArrayList<>();

        if (jobRunConfig.getTakeScreenshots() != null) {
            envList.add(format("TAKE_SCREENSHOTS={0}", jobRunConfig.getTakeScreenshots()));
        }
        envList.add(format("ATFACTORY_PULL={0}", prependBaseUrl(jobRunConfig.getPullUrl())));
        envList.add(format("ATFACTORY_PUSH={0}", prependBaseUrl(jobRunConfig.getPushUrl())));

        if (jobRunConfig.getScenarioPattern() != null) {
            envList.add(format("SCENARIO_PATTERN={0}", jobRunConfig.getScenarioPattern()));
        }

        if (jobRunConfig.getDisplayGeometry() != null) {
            envList.add(format("GEOMETRY={0}", jobRunConfig.getDisplayGeometry()));
        }

        // TODO add support for context
        Map<String, String> context = jobRunConfig.getContext();
        if (!context.isEmpty()) {
            envList.add(format("CONTEXT={0}", context));
        }

        return envList;
    }

    private String prependBaseUrl(String uri) {
        if (!uri.startsWith("http://") && !uri.startsWith("https://")) {
            return runnerSettings.getApiUrl() + uri;
        }

        return uri;
    }
}
