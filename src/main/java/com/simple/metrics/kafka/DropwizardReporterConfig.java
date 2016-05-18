package com.simple.metrics.kafka;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class DropwizardReporterConfig extends AbstractConfig {
    public static final String DEFAULT_REGISTRY_NAME = "default";
    public static final String REGISTRY_PROPERTY_NAME = "metric.dropwizard.registry";

    private static final ConfigDef configDefinition = new ConfigDef()
            .define(REGISTRY_PROPERTY_NAME, ConfigDef.Type.STRING, DEFAULT_REGISTRY_NAME, ConfigDef.Importance.LOW,
                    "Name of the dropwizard-metrics registry to use; passed to SharedMetricRegistries.getOrCreate");

    DropwizardReporterConfig(Map<?, ?> props) {
        super(configDefinition, props);
    }
}
