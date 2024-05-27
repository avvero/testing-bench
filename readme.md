# Testcontainers-based Load Testing Bench

This module serves as a load testing stand for demonstrating load testing approach using Spock tests with Testcontainers 
in a Gradle environment within a Gradle project.

Supported load-testing tools:
- Gatling

### Usage
1. Ensure Java and Gradle are installed on your system.
2. Clone this repository to your local machine.
3. Navigate to the root directory of the project.
4. Open a terminal and execute the following command to run the load testing:

```bash
./gradlew :load-tests:test --tests "pw.avvero.spring.sandbox.GatlingTests"
```

### Reporting

Performance results, including metrics and logs, are saved in the `build/${timestamp}` directory, where `${timestamp}` 
represents a timestamp indicating each test run in the ISO 8601 format.
List of Report Entities:
- WireMock Logs
- Gatling Report
- Garbage Collector logs
- *Target Service Logs
- Gatling Logs
- JFR (Java Flight Recording)

```bash
performance/
|-- build/
|   |-- ${timestamp}/
|   |   |-- gatling-results/
|   |   |-- jfr/
|   |   |   |-- flight.jfr
|   |   |-- logs/
|   |   |   |-- dps.log
|   |   |   |-- gatling.log
|   |   |   |-- gc.log
|   |   |   |-- wiremock.log
|   |   |   |-- wrk.log
|   |   |   |-- ...
|   |-- ${timestamp}/
|   |-- ...
```

### Additional Information
- For customizing the load testing scenarios or configurations, refer to the Spock tests in the `src/test/groovy` directory of the `load-tests` module.
- Ensure Docker is installed and running on your machine, as Testcontainers rely on Docker for container management during test execution.

### Known issues

#### Settings for JFR
Using options like `dumponexit=true,disk=true` with `-XX:StartFlightRecording:settings=` should ideally force JFR 
to write metrics. However, in some cases, this doesn't work as expected. To ensure proper recording, it's necessary 
to manipulate timings. Please ensure that tests run for a duration longer than the `duration=120s` setting.

### Yandex tank
```python
echo "POST||/weather/getForecast||get forecast||{\"city\": \"London\"}" | ./make_ammo.py
echo "POST||/weather/getForecast||get_forecast||{\"city\": \"London\"}" | python3 load-tests/src/test/resources/yandex-tank/make_ammo.py
```