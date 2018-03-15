# Runner 

[![codecov.io](https://codecov.io/github/automate-website/runner/coverage.svg?branch=master)](https://codecov.io/github/automate-website/jwebrobot?branch=master)

## How to Run

    docker run \
        -v /var/run/docker.sock:/var/run/docker.sock \
        -e WEBSITE_AUTOMATE_RUNNER_ID=<Runner ID> \
        automatewebsite/runner
