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
    private static final String TAKE_SCREENSHOTS_TEMPLATE = "TAKE_SCREENSHOTS={0}";
    private static final String PULL_URL_TEMPLATE = "PULL_URL={0}";
    private static final String PUSH_URL_TEMPLATE = "PUSH_URL={0}";
    private static final String SCENARIO_PATTERN_TEMPLATE = "SCENARIO_PATTERN={0}";
    private static final String GEOMETRY_TEMPLATE = "GEOMETRY={0}";
    private static final String CONTEXT_TEMPLATE = "CONTEXT={0}";

    @Autowired
    private RunnerSettings runnerSettings;

    public List<String> toEnv(JobRunConfig jobRunConfig) {
        List<String> envList = new ArrayList<>();

        if (jobRunConfig.getTakeScreenshots() != null) {
            envList.add(format(TAKE_SCREENSHOTS_TEMPLATE, jobRunConfig.getTakeScreenshots()));
        }
        envList.add(format(PULL_URL_TEMPLATE, prependBaseUrl(jobRunConfig.getPullUrl())));
        envList.add(format(PUSH_URL_TEMPLATE, prependBaseUrl(jobRunConfig.getPushUrl())));

        if (jobRunConfig.getScenarioPattern() != null) {
            envList.add(format(SCENARIO_PATTERN_TEMPLATE, jobRunConfig.getScenarioPattern()));
        }

        if (jobRunConfig.getDisplayGeometry() != null) {
            envList.add(format(GEOMETRY_TEMPLATE, jobRunConfig.getDisplayGeometry()));
        }

        // TODO add support for context
        Map<String, String> context = jobRunConfig.getContext();
        if (!context.isEmpty()) {
            envList.add(format(CONTEXT_TEMPLATE, context));
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
