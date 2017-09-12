package com.simple.metrics.kafka;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class DropwizardReporterConfig extends AbstractConfig {
    public static final String DEFAULT_REGISTRY_NAME = "default";
    public static final String REGISTRY_PROPERTY_NAME = "metric.dropwizard.registry";

    public static final String GRAPHITE_HOST_PROPERTY_NAME = "metric.dropwizard.graphite.host";
    public static final String GRAPHITE_PORT_PROPERTY_NAME = "metric.dropwizard.graphite.port";
    public static final String GRAPHITE_PREFIX_PROPERTY_NAME = "metric.dropwizard.graphite.prefix";

    private static final ConfigDef CONFIG = new ConfigDef()
        .define(REGISTRY_PROPERTY_NAME, ConfigDef.Type.STRING, DEFAULT_REGISTRY_NAME, ConfigDef.Importance.LOW,
            "Name of the dropwizard-metrics registry to use; passed to SharedMetricRegistries.getOrCreate")
        .define(GRAPHITE_HOST_PROPERTY_NAME, ConfigDef.Type.STRING, "localhost", ConfigDef.Importance.LOW,
                "Destination host for the GraphiteReporter (default: localhost); only relevant for DropwizardReporterGraphite")
        .define(GRAPHITE_PORT_PROPERTY_NAME, ConfigDef.Type.INT, 2003, ConfigDef.Importance.LOW,
                "Destination port for the GraphiteReporter (default: 2003); only relevant for DropwizardReporterGraphite")
        .define(GRAPHITE_PREFIX_PROPERTY_NAME, ConfigDef.Type.STRING, "", ConfigDef.Importance.LOW,
                "Metric prefix for metrics published by the GraphiteReporter; only relevant for DropwizardReporterGraphite")
        ;

    DropwizardReporterConfig(Map<?, ?> props) {
        super(CONFIG, props);
    }

    /**
     * Helper method for generating documentation for the config.
     */
    public static void main(String[] args) {
        System.out.println(CONFIG.toHtmlTable());
    }
}
