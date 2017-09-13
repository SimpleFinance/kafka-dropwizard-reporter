# kafka-dropwizard-reporter [![Build Status](https://travis-ci.org/SimpleFinance/kafka-dropwizard-reporter.svg?branch=master)](https://travis-ci.org/SimpleFinance/kafka-dropwizard-reporter)

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
Also functions with Kafka Streams and Kafka Connect.


## Usage

First, declare a dependency on this package and on the explicit versions
of the dependencies that you want:
```xml
      <dependency>
          <groupId>com.simple</groupId>
          <artifactId>kafka-dropwizard-reporter</artifactId>
          <version>1.1.0</version>
      </dependency>

      <!-- Required; you must provide metrics-core and kafka-clients;
           versions can be more recent than those shown below -->
      <dependency>
          <groupId>io.dropwizard.metrics</groupId>
          <artifactId>metrics-core</artifactId>
          <version>3.1.2</version>
      </dependency>
      <dependency>
          <groupId>org.apache.kafka</groupId>
          <artifactId>kafka-clients</artifactId>
          <version>0.9.0.1</version>
          <scope>provided</scope>
      </dependency>

      <!-- Optional; the user is expected to specify this dependency
           explicitly if DropwizardReporterGraphite is to be used -->
      <dependency>
          <groupId>io.dropwizard.metrics</groupId>
          <artifactId>metrics-graphite</artifactId>
          <version>3.1.2</version>
          <scope>provided</scope>
      </dependency>
```

Then, include the `DropwizardReporter` class in the properties you pass
to producers, consumers, and `KafkaStreams` applications:
```
metric.reporters=com.simple.metrics.kafka.DropwizardReporter
```

That client will now automatically register all of its built-in
metrics with a Dropwizard `MetricRegistry` when it's initialized.
The registry is discovered by calling
`SharedMetricRegistries.getOrCreate("default")`,
so to direct `DropwizardReporter` to a particular registry, make
sure to call `SharedMetricRegistries.add("default", myRegistry)`
before instantiating Kafka clients if you want metrics to belong
to `myRegistry`.

For a full example of integrating Kafka client metrics in a Dropwizard
application, see [example/](example/).

## Reporting to Graphite

If your application is not already handling reporting of metrics to an external
source, you can use `com.simple.metrics.kafka.DropwizardReporterGraphite`
which adds instantiation of a `GraphiteReporter`.
Make sure you've declared a dependency on `metrics-graphite`, then
see <a href="configuration">Configuration</a> below.

## Configuration

The following configuration options are available:

<table class="data-table"><tbody>
<tr>
<th>Name</th>
<th>Description</th>
<th>Type</th>
<th>Default</th>
<th>Valid Values</th>
<th>Importance</th>
</tr>
<tr>
<td>metric.dropwizard.graphite.host</td><td>Destination host for the GraphiteReporter (default: localhost); only relevant for DropwizardReporterGraphite</td><td>string</td><td>localhost</td><td></td><td>low</td></tr>
<tr>
<td>metric.dropwizard.graphite.port</td><td>Destination port for the GraphiteReporter (default: 2003); only relevant for DropwizardReporterGraphite</td><td>int</td><td>2003</td><td></td><td>low</td></tr>
<tr>
<td>metric.dropwizard.graphite.prefix</td><td>Metric prefix for metrics published by the GraphiteReporter; only relevant for DropwizardReporterGraphite</td><td>string</td><td>""</td><td></td><td>low</td></tr>
<tr>
<td>metric.dropwizard.registry</td><td>Name of the dropwizard-metrics registry to use; passed to SharedMetricRegistries.getOrCreate</td><td>string</td><td>default</td><td></td><td>low</td></tr>
</tbody></table>

If you'd like to send Kafka metrics to a separate `MetricRegistry` instance,
you can pass a name as `metric.dropwizard.registry` when configuring the client.
For example, the following would end up calling
`SharedMetricRegistries.getOrCreate("kafka-metrics")`:
```
metric.reporters=com.simple.metrics.kafka.DropwizardReporter
metric.dropwizard.registry=kafka-metrics
```

A configuration using Graphite reporting could be:
```
metric.reporters=com.simple.metrics.kafka.DropwizardGraphiteReporter
metric.dropwizard.graphite.host=localhost
metric.dropwizard.graphite.port=2003
metric.dropwizard.graphite.prefix=mycompany.myproject
```

Note that usage of these configuration options might trigger warning messages like
`org.apache.kafka.clients.consumer.ConsumerConfig: The configuration metric.dropwizard.registry = kafka-metrics was supplied but isn't a known config.`
due to a bug in Kafka's configuration machinery present until at least version 0.10.0.0.
This was addressed in [KAFKA-3711](https://issues.apache.org/jira/browse/KAFKA-3711).

## Building

To build the project, you'll need to
[install Apache Maven 3](https://maven.apache.org/install.html).
If you're on Mac OS X, you can install Maven via [Homebrew](http://brew.sh/):

    $ brew install maven

Once that's installed, run the following from main directory
(where `pom.xml` lives):
```
mvn clean install
```

## Deploying

To deploy to Maven Central, you'll need to provide GPG signatures for all
the artifacts. There's a `sign` profile for this purpose:

    mvn clean verify -Psign

Check that the output looks good. You should a jar, sources, javadoc, and pom,
each with a signed `.asc` companion:
``` bash
$ ls target/kafka-*
target/kafka-dropwizard-reporter-1.0.1-javadoc.jar      target/kafka-dropwizard-reporter-1.0.1.jar
target/kafka-dropwizard-reporter-1.0.1-javadoc.jar.asc  target/kafka-dropwizard-reporter-1.0.1.jar.asc
target/kafka-dropwizard-reporter-1.0.1-sources.jar      target/kafka-dropwizard-reporter-1.0.1.pom
target/kafka-dropwizard-reporter-1.0.1-sources.jar.asc  target/kafka-dropwizard-reporter-1.0.1.pom.asc
```

If the `target` directory looks good, deploy:

    mvn deploy -Psign

You'll need to have an account with Maven Central as part of the `com.simple`
group. Contact the project maintainer for more info.
Visit https://oss.sonatype.org/index.html#stagingRepositories
and close the new staging repository (named something like `comsimple-XXXX`).
Wait a few minutes, then select the repo again and "Release" it.
If there is no "Release" button, something is wrong with your artifacts,
or they haven't been closed out yet.

Once released, it will take some time for Maven Central to sync and for the
artifact to be available.

## Contributing

Contributions, feature requests, and bug reports are all welcome.
Feel free to [submit an issue](issues/new)
or submit a pull request to this repo.

## Changelog

Check the [releases](https://github.com/SimpleFinance/kafka-dropwizard-reporter/releases)
page for summaries of what has changed in each release.

## Also See

This project takes significant inspiration from the `GraphiteReporter` class
defined in [apakulov/kafka-graphite](https://github.com/apakulov/kafka-graphite).
