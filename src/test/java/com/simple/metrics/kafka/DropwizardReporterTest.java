package com.simple.metrics.kafka;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.SharedMetricRegistries;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.MetricConfig;
import org.apache.kafka.common.metrics.Metrics;
import org.apache.kafka.common.metrics.Sensor;
import org.apache.kafka.common.metrics.stats.Avg;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DropwizardReporterTest {
    @SuppressWarnings("rawtypes")
    @Test
    public void testMetricChange() {
        Metrics metrics = new Metrics();
        DropwizardReporter reporter = new DropwizardReporter();
        reporter.configure(new HashMap<>());
        metrics.addReporter(reporter);
        Sensor sensor = metrics.sensor("kafka.requests");
        sensor.add(new MetricName("pack.bean1.avg", "grp1", "", new HashMap<>()), new Avg());

        Map<String, Gauge> gauges = SharedMetricRegistries.getOrCreate("default").getGauges();
        String expectedName = "org.apache.kafka.common.metrics.grp1.pack.bean1.avg";
        Assert.assertTrue(gauges.containsKey(expectedName));

        sensor.record(2.1);
        sensor.record(2.2);
        sensor.record(2.6);
        Assert.assertEquals(2.3, (Double) gauges.get(expectedName).getValue(), 0.001);
    }

    @Test
    public void testNonNumericMetricYieldsNullValue() {
        Metrics metrics = new Metrics();
        DropwizardReporter reporter = new DropwizardReporter();
        reporter.configure(new HashMap<>());
        metrics.addReporter(reporter);
        metrics.addMetric(new MetricName("metric", "non-numeric", "", new HashMap<>()), new ConstantGauge<>("non-numeric"));
        String expectedName = "org.apache.kafka.common.metrics.non-numeric.metric";
        Assert.assertTrue(reporter.registry.getGauges().containsKey(expectedName));
        Assert.assertNull(reporter.registry.getGauges().get(expectedName).getValue());
    }

    @Test
    public void testNumericMetricYieldsActualValue() {
        Metrics metrics = new Metrics();
        DropwizardReporter reporter = new DropwizardReporter();
        reporter.configure(new HashMap<>());
        metrics.addReporter(reporter);
        metrics.addMetric(new MetricName("metric", "numeric", "", new HashMap<>()),
                new ConstantGauge<>(12));
        String expectedName = "org.apache.kafka.common.metrics.numeric.metric";
        Assert.assertTrue(reporter.registry.getGauges().containsKey(expectedName));
        Assert.assertEquals((Double) reporter.registry.getGauges().get(expectedName).getValue(), 12.0, 0.001);
    }

    @Test
    public void testNullMetricYieldsNull() {
        Metrics metrics = new Metrics();
        DropwizardReporter reporter = new DropwizardReporter();
        reporter.configure(new HashMap<>());
        metrics.addReporter(reporter);
        metrics.addMetric(new MetricName("metric", "numeric", "", new HashMap<>()),
                new ConstantGauge<Double>(null));
        String expectedName = "org.apache.kafka.common.metrics.numeric.metric";
        Assert.assertTrue(reporter.registry.getGauges().containsKey(expectedName));
        Assert.assertEquals((Double) reporter.registry.getGauges().get(expectedName).getValue(), 12.0, 0.001);
    }

    private static class ConstantGauge<T> implements org.apache.kafka.common.metrics.Gauge<T> {
        private final T value;

        public ConstantGauge(T value) {
            this.value = value;
        }

        @Override
        public T value(MetricConfig metricConfig, long l) {
            return value;
        }
    }
}
