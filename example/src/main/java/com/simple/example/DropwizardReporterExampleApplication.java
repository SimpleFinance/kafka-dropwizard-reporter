package com.simple.example;

import com.codahale.metrics.SharedMetricRegistries;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

public class DropwizardReporterExampleApplication extends Application<DropwizardReporterExampleConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropwizardReporterExampleApplication.class);

    public static void main(final String[] args) throws Exception {
        new DropwizardReporterExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropwizardReporterExample";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardReporterExampleConfiguration> bootstrap) {
        // No initialization needed.
    }

    @Override
    public void run(final DropwizardReporterExampleConfiguration configuration,
                    final Environment environment) {

        // The default shared registry will be pre-registered in Dropwizard 1.0+
        SharedMetricRegistries.add("default", environment.metrics());

        final ExecutorService executorService = environment
                .lifecycle()
                .executorService("kafka-threads")
                .minThreads(2)
                .maxThreads(10)
                .build();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Producer<String, String> producer = new KafkaProducer<>(configuration.getProducer());
                int i = 0;
                try {
                    while (true) {
                        String s = Integer.toString(i);
                        producer.send(new ProducerRecord<String, String>("my-topic", s, s));
                        i++;
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    LOGGER.info("Producer interrupted");
                    producer.close();
                }
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Consumer<String, String> consumer = new KafkaConsumer<>(configuration.getConsumer());
                consumer.subscribe(Arrays.asList("my-topic"));
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(2000);
                    for (ConsumerRecord<String, String> record : records)
                        LOGGER.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
                }
            }
        });

    }
}
