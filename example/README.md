## Integration with Dropwizard Applications

`DropwizardReporter` calls `SharedMetricRegistries.getOrCreate("default")`
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


## Running the Example

This example application starts up a `KafkaProducer` and a `KafkaConsumer`
inside an `ExecutorService` to demonstrate what metrics are produced.

If you want to run this yourself, you'll need to have Zookeeper and Kafka
running locally on their default ports.

Then,

```
# Move into the example/ directory.
cd example/

# Compile the application.
mvn clean package

# Start the server.
java -jar target/kafka-dropwizard-reporter-example-1.0-SNAPSHOT.jar server conf.yml
```


## Example Output

While the application is running, you should be able to see the Kafka client
metrics by hitting the `/metrics` endpoint on the admin port.
Notice that the `client.id` property of the producer and consumer is reflected
in the metric names.
```
$ http localhost:8081/metrics
{
...
    "gauges": {
    ...
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.assigned-partitions": {
            "value": 1.0
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.commit-latency-avg": {
            "value": 3.0
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.commit-latency-max": {
            "value": 3.0
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.commit-rate": {
            "value": 0.03322479898996611
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.heartbeat-rate": {
            "value": 0.031151677517834337
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.heartbeat-response-time-max": {
            "value": 1.0
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.join-rate": {
            "value": 0.028481913984619765
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.join-time-avg": {
            "value": 4.0
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.join-time-max": {
            "value": 4.0
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.last-heartbeat-seconds-ago": {
            "value": 2.0
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.sync-rate": {
            "value": 0.028483536515893818
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.sync-time-avg": {
            "value": 2.0
        },
        "org.apache.kafka.common.metrics.consumer-coordinator-metrics.my-consumer-id.sync-time-max": {
            "value": 2.0
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.bytes-consumed-rate": {
            "value": 10.54131054131054
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.fetch-latency-avg": {
            "value": 309.4375
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.fetch-latency-max": {
            "value": 504.0
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.fetch-rate": {
            "value": 0.45584045584045585
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.fetch-size-avg": {
            "value": 23.125
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.fetch-size-max": {
            "value": 88.0
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.fetch-throttle-time-avg": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.fetch-throttle-time-max": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.records-consumed-rate": {
            "value": 0.370359818808581
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.records-lag-max": {
            "value": 1.0
        },
        "org.apache.kafka.common.metrics.consumer-fetch-manager-metrics.my-consumer-id.records-per-request-avg": {
            "value": 0.8125
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.connection-close-rate": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.connection-count": {
            "value": 3.0
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.connection-creation-rate": {
            "value": 0.08519338899301415
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.incoming-byte-rate": {
            "value": 42.536229820915075
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.io-ratio": {
            "value": 0.0003977566377963936
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.io-time-ns-avg": {
            "value": 264283.0188679245
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.io-wait-ratio": {
            "value": 0.14415233254777252
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.io-wait-time-ns-avg": {
            "value": 95790584.90566038
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.network-io-rate": {
            "value": 1.3381544856646643
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.outgoing-byte-rate": {
            "value": 55.49070409703044
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.request-rate": {
            "value": 0.6833129288500414
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.request-size-avg": {
            "value": 81.20833333333333
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.request-size-max": {
            "value": 192.0
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.response-rate": {
            "value": 0.6548229131078465
        },
        "org.apache.kafka.common.metrics.consumer-metrics.my-consumer-id.select-rate": {
            "value": 1.5048268029528677
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node--1.incoming-byte-rate": {
            "value": 2.192233230839312
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node--1.outgoing-byte-rate": {
            "value": 1.1957635804578066
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node--1.request-latency-avg": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node--1.request-latency-max": {
            "value": "-Infinity"
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node--1.request-rate": {
            "value": 0.028470561439471586
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node--1.request-size-avg": {
            "value": 42.0
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node--1.request-size-max": {
            "value": 42.0
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node--1.response-rate": {
            "value": 0.028469750889679714
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-0.incoming-byte-rate": {
            "value": 31.405711682469175
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-0.outgoing-byte-rate": {
            "value": 37.12772621149137
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-0.request-latency-avg": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-0.request-latency-max": {
            "value": "-Infinity"
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-0.request-rate": {
            "value": 0.5124992881954331
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-0.request-size-avg": {
            "value": 72.44444444444444
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-0.request-size-max": {
            "value": 74.0
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-0.response-rate": {
            "value": 0.48404088721847327
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-2147483647.incoming-byte-rate": {
            "value": 8.942047558023637
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-2147483647.outgoing-byte-rate": {
            "value": 17.171659642328283
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-2147483647.request-latency-avg": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-2147483647.request-latency-max": {
            "value": "-Infinity"
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-2147483647.request-rate": {
            "value": 0.14238118290286758
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-2147483647.request-size-avg": {
            "value": 120.6
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-2147483647.request-size-max": {
            "value": 192.0
        },
        "org.apache.kafka.common.metrics.consumer-node-metrics.my-consumer-id.node-2147483647.response-rate": {
            "value": 0.14238523749857615
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.batch-size-avg": {
            "value": 28.181818181818183
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.batch-size-max": {
            "value": 30.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.buffer-available-bytes": {
            "value": 33554432.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.buffer-exhausted-rate": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.buffer-total-bytes": {
            "value": 33554432.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.bufferpool-wait-ratio": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.compression-rate-avg": {
            "value": 1.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.connection-close-rate": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.connection-count": {
            "value": 2.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.connection-creation-rate": {
            "value": 0.0568197960169323
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.incoming-byte-rate": {
            "value": 14.690003977950786
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.io-ratio": {
            "value": 0.0003581031255317942
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.io-time-ns-avg": {
            "value": 323743.58974358975
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.io-wait-ratio": {
            "value": 0.14408179703896987
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.io-wait-time-ns-avg": {
            "value": 130257333.33333333
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.metadata-age": {
            "value": 5.195
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.network-io-rate": {
            "value": 0.681856923688846
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.outgoing-byte-rate": {
            "value": 28.75163361554634
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.produce-throttle-time-avg": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.produce-throttle-time-max": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.record-error-rate": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.record-queue-time-avg": {
            "value": 0.5454545454545454
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.record-queue-time-max": {
            "value": 4.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.record-retry-rate": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.record-send-rate": {
            "value": 0.312659882894662
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.record-size-avg": {
            "value": 16.181818181818183
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.record-size-max": {
            "value": 18.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.records-per-request-avg": {
            "value": 1.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.request-latency-avg": {
            "value": 1.6363636363636365
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.request-latency-max": {
            "value": 3.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.request-rate": {
            "value": 0.3409187761015938
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.request-size-avg": {
            "value": 84.33333333333333
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.request-size-max": {
            "value": 90.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.requests-in-flight": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.response-rate": {
            "value": 0.34093814813762535
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.select-rate": {
            "value": 1.1060378321658488
        },
        "org.apache.kafka.common.metrics.producer-metrics.my-producer-id.waiting-threads": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node--1.incoming-byte-rate": {
            "value": 2.1876864505497626
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node--1.outgoing-byte-rate": {
            "value": 1.1931818181818181
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node--1.request-latency-avg": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node--1.request-latency-max": {
            "value": "-Infinity"
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node--1.request-rate": {
            "value": 0.028408283855572286
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node--1.request-size-avg": {
            "value": 42.0
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node--1.request-size-max": {
            "value": 42.0
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node--1.response-rate": {
            "value": 0.028410705153701914
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node-0.incoming-byte-rate": {
            "value": 12.50675080299025
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node-0.outgoing-byte-rate": {
            "value": 27.570916946165653
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node-0.request-latency-avg": {
            "value": 1.6363636363636365
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node-0.request-latency-max": {
            "value": 3.0
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node-0.request-rate": {
            "value": 0.3126509962197652
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node-0.request-size-avg": {
            "value": 88.18181818181819
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node-0.request-size-max": {
            "value": 90.0
        },
        "org.apache.kafka.common.metrics.producer-node-metrics.my-producer-id.node-0.response-rate": {
            "value": 0.312659882894662
        },
        "org.apache.kafka.common.metrics.producer-topic-metrics.my-producer-id.my-topic.byte-rate": {
            "value": 8.810823101409733
        },
        "org.apache.kafka.common.metrics.producer-topic-metrics.my-producer-id.my-topic.compression-rate": {
            "value": 1.0
        },
        "org.apache.kafka.common.metrics.producer-topic-metrics.my-producer-id.my-topic.record-error-rate": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-topic-metrics.my-producer-id.my-topic.record-retry-rate": {
            "value": 0.0
        },
        "org.apache.kafka.common.metrics.producer-topic-metrics.my-producer-id.my-topic.record-send-rate": {
            "value": 0.3126332243853915
        },
    ...
}
```
