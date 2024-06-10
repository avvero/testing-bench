# Testcontainers-based Load Testing Bench

This module serves as a load testing stand for demonstrating load testing approach using Spock tests with Testcontainers 
in a Gradle environment within a Gradle project.

Supported load-testing tools:
- Gatling
- Wrk
- Yandex.Tank

## References
- [Testcontainers-based Load Testing Bench - Medium Article](https://medium.com/@avvero.abernathy/testcontainers-based-load-testing-bench-112a275f549d)
- [Testcontainers-based Load Testing Bench - Habr Article](https://habr.com/ru/articles/819589/)

## Project structure 

```properties
load-tests/
|-- src/
|   |-- gatling/
|   |   |-- scala/
|   |   |   |-- MainSimulation.scala         # Main Gatling simulation file
|   |   |-- resources/
|   |   |   |-- gatling.conf                 # Gatling configuration file
|   |   |   |-- logback-test.xml             # Logback configuration for testing
|   |-- test/
|   |   |-- groovy/
|   |   |   |-- pw.avvero.spring.sandbox/
|   |   |   |   |-- GatlingTests.groovy      # Gatling load test file
|   |   |   |   |-- WrkTests.groovy          # Wrk load test file
|   |   |   |   |-- YandexTankTests.groovy   # Yandex.Tank load test file
|   |   |-- java/
|   |   |   |-- pw.avvero.spring.sandbox/
|   |   |   |   |-- FileHeadLogConsumer.java # Helper class for logging to a file
|   |   |-- resources/
|   |   |   |-- wiremock/
|   |   |   |   |-- mappings/                # WireMock setup for mocking external services
|   |   |   |   |   |-- health.json          
|   |   |   |   |   |-- forecast.json
|   |   |   |-- yandex-tank/                 # Yandex.Tank load testing configuration
|   |   |   |   |-- ammo.txt
|   |   |   |   |-- load.yaml
|   |   |   |   |-- make_ammo.py
|   |   |   |-- wrk/                         # LuaJIT scripts for Wrk
|   |   |   |   |-- scripts/                 
|   |   |   |   |   |-- getForecast.lua
|-- build.gradle
```

## Usage
1. Ensure Java and Gradle are installed on your system.
2. Clone this repository to your local machine.
3. Navigate to the root directory of the project.
4. Open a terminal and execute the following command to run the load testing:

```bash
./gradlew :load-tests:test --tests "pw.avvero.spring.sandbox.GatlingTests"
```

## Reporting

Test results, including JVM performance recordings and logs, are saved in the directory `build/${timestamp}`, where 
`${timestamp}` represents the timestamp of each test run.

The following reports will be available for review:
- Garbage Collector logs.
- WireMock logs.
- Target service logs.
- Wrk logs.
- JFR (Java Flight Recording).

If Gatling was used:
- Gatling report.
- Gatling logs.

If Wrk was used:
- Wrk logs.

If Yandex.Tank was used:
- Yandex.Tank result files, with an additional upload to [Overload](https://overload.yandex.net/).
- Yandex.Tank logs.

The directory structure for the reports is as follows:
```
load-tests/
|-- build/
|   |-- ${timestamp}/
|   |   |-- gatling-results/
|   |   |-- jfr/
|   |   |-- yandex-tank/
|   |   |-- logs/
|   |   |   |-- sandbox.log
|   |   |   |-- gatling.log
|   |   |   |-- gc.log
|   |   |   |-- wiremock.log
|   |   |   |-- wrk.log
|   |   |   |-- yandex-tank.log
|   |-- ${timestamp}/
|   |-- ...
```

## Known issues

### Settings for JFR
Using options like `dumponexit=true,disk=true` with `-XX:StartFlightRecording:settings=` should ideally force JFR 
to write metrics. However, in some cases, this doesn't work as expected. To ensure proper recording, it's necessary 
to manipulate timings. Please ensure that tests run for a duration longer than the `duration=120s` setting.

### Yandex tank ammo configuration
```python
echo "POST||/weather/getForecast||get_forecast||{\"city\": \"London\"}" | python3 load-tests/src/test/resources/yandex-tank/make_ammo.py
```