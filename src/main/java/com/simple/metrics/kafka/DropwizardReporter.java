package com.simple.metrics.kafka;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DropwizardReporter implements MetricsReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DropwizardReporter.class);

    private MetricRegistry registry;
    private DropwizardReporterConfig config;

    @Override
    public void configure(Map<String, ?> configs) {
        this.config = new DropwizardReporterConfig(configs);
    }

    @Override
    public void init(List<KafkaMetric> list) {
        if (config == null) {
            throw new IllegalStateException("Must call configure() before calling init() on a reporter.");
        }
        String registryName = config.getString(DropwizardReporterConfig.REGISTRY_PROPERTY_NAME);
        this.registry = SharedMetricRegistries.getOrCreate(registryName);
        for (KafkaMetric kafkaMetric : list) {
            this.metricChange(kafkaMetric);
        }
    }

    @Override
    public void metricChange(final KafkaMetric kafkaMetric) {
        LOGGER.debug("Processing a metric change for {}", kafkaMetric.metricName().toString());
        String name = dropwizardMetricName(kafkaMetric);
        Gauge<Double> gauge = new Gauge<Double>() {
            @Override
            public Double getValue() {
                return kafkaMetric.value();
            }
        };
        if (registry.getGauges().containsKey(name)) {
            LOGGER.debug("{} was already registered, so first removing.", name);
            registry.remove(name);
        }
        LOGGER.debug("Registering {}", name);
        registry.register(name, gauge);
    }

    @Override
    public void metricRemoval(KafkaMetric kafkaMetric) {
        String name = dropwizardMetricName(kafkaMetric);
        LOGGER.debug("Removing {}", name);
        registry.remove(name);
    }

    @Override
    public void close() {}

    private String dropwizardMetricName(KafkaMetric kafkaMetric) {
        MetricName name = kafkaMetric.metricName();

        List<String> nameParts = new ArrayList<String>(2);
        nameParts.add(name.group());
        nameParts.addAll(name.tags().values());
        nameParts.add(name.name());

        StringBuilder builder = new StringBuilder();
        for (String namePart : nameParts) {
            builder.append(namePart);
            builder.append(".");
        }
        builder.setLength(builder.length() - 1);  // Remove the trailing dot.
        String processedName = builder.toString().replace(' ', '_').replace("\\.", "_");

        return MetricRegistry.name(MetricsReporter.class, processedName);
    }

}
