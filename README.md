# kafka-dropwizard-reporter

This package provides a `DropwizardReporter` class that connects the
built-in metrics maintained by Kafka's client libraries with
Dropwizard Metrics 3.0+.
The Kafka metrics are added as `Gauge` instances to a Dropwizard
`MetricRegistry` instance.

If you're already using Dropwizard Metrics in your application
to serve metrics via HTTP, Graphite, StatsD, etc.,
this reporter provides an easy bridge to pass Kafka consumer,
producer, and streams metrics to those same outputs.

## Compatibility

`dropwizard-metrics` 3.0 and above.

`kafka-clients` 0.8.1 and above, including 0.9 and 0.10.
Also functions with the new `kafka-streams` library in 0.10.0.0.

## Usage

First, declare a dependency on this package:
```xml
      <dependency>
          <groupId>com.simple</groupId>
          <artifactId>kafka-dropwizard-reporter</artifactId>
          <version>1.0.0</version>
      </dependency>
```

This package expects that `dropwizard-metrics` and `kafka-clients` libraries
are also defined in your project.

Then, include the `DropwizardReporter` class in the properties you pass
to producers, consumers, and `KafkaStreams` applications:
```
metric.reporters=com.simple.metrics.kafka.DropwizardReporter
```

That client will now automatically register all of its built-in
metrics with a Dropwizard `MetricRegistry` when it's initialized.

## Integration with Dropwizard Applications

The reporter calls `SharedMetricRegistries.getOrCreate("default")`
to discover a registry. To make sure `DropwizardReporter` instances report
to the main metrics registry of a Dropwizard application, call
the following in your `run` method before defining any Kafka clients
([this will be done by default in Dropwizard 1.0+](https://github.com/dropwizard/dropwizard/pull/1513)):
```java
public class MyApplication extends Application<MyConfiguration> {
// [...]
    @Override
    public void run(MyConfiguration configuration, environment: Environment) {
        SharedMetricRegistries.add("default", environment.metrics());
    // [...]
    }
}
```

Metrics will be published with a prefix of
`<metrics-prefix>.org.apache.common.metrics.MetricRegistry`
where `metric-prefix` is the global prefix you've configured
for your reporters.

## Configuration (Optional)

If you'd like to send Kafka metrics to a separate `MetricRegistry` instance,
you can pass a name as `metric.dropwizard.registry` when configuring the client.
For example, the following would end up calling
`SharedMetricRegistries.getOrCreate("kafka-metrics")`:
```
metric.reporters=com.simple.metrics.kafka.DropwizardReporter
metric.dropwizard.registry=kafka-metrics
```

## Building

To build the project, you'll need to
[install Apache Maven 3](https://maven.apache.org/install.html).
If you're on Mac OS X, installation via [Homebrew](http://brew.sh/)
is recommended (`brew install maven`).
Once that's installed, run the following from main directory
(where `pom.xml` lives):
```
mvn clean install
```

## Contributing

Contributions, feature requests, and bug reports are all welcome.
Feel free to [submit an issue](issues/new)
or submit a pull request to this repo.

## Also See

This project takes significant inspiration from the `GraphiteReporter` class
defined in [apakulov/kafka-graphite](https://github.com/apakulov/kafka-graphite).
