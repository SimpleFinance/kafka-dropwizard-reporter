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
import java.util.Set;
import java.util.HashSet;

public class DropwizardReporter implements MetricsReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DropwizardReporter.class);
    protected static final String METRIC_PREFIX = MetricsReporter.class.getPackage().getName();

    protected MetricRegistry registry;
    protected DropwizardReporterConfig config;
    private final Set<String> metricNames = new HashSet<>();

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

        Gauge<Double> gauge = () -> {
            if (kafkaMetric.metricValue() instanceof Number) {
                return ((Number) kafkaMetric.metricValue()).doubleValue();
            } else {
                // Null values are not reported by GraphiteReporter
                return null;
            }
        };
        LOGGER.debug("Registering {}", name);
        try {
            registry.register(name, gauge);
            metricNames.add(name);
        } catch (IllegalArgumentException e) {
            LOGGER.debug("metricChange called for `{}' which was already registered, ignoring.", name);
        }
    }

    @Override
    public void metricRemoval(KafkaMetric kafkaMetric) {
        String name = dropwizardMetricName(kafkaMetric);
        LOGGER.debug("Removing {}", name);
        registry.remove(name);
        metricNames.remove(name);
    }

    @Override
    public void close() {
        for (String name : metricNames)
            registry.remove(name);
    }

    private static String dropwizardMetricName(KafkaMetric kafkaMetric) {
        MetricName name = kafkaMetric.metricName();

        List<String> nameParts = new ArrayList<>(2);
        nameParts.add(name.group());
        nameParts.addAll(name.tags().values());
        nameParts.add(name.name());

        String processedName = String.join(".", nameParts)
                .replace(' ', '_')
                .replace("\\.", "_");

        return MetricRegistry.name(METRIC_PREFIX, processedName);
    }

}
