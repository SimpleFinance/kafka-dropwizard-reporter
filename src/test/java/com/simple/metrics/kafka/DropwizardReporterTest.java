package com.simple.metrics.kafka;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.SharedMetricRegistries;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.Metrics;
import org.apache.kafka.common.metrics.Sensor;
import org.apache.kafka.common.metrics.stats.Avg;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DropwizardReporterTest {
    @Test
    public void testMetricChange() throws Exception {
        Metrics metrics = new Metrics();
        DropwizardReporter reporter = new DropwizardReporter();
        reporter.configure(new HashMap<String, Object>());
        metrics.addReporter(reporter);
        Sensor sensor = metrics.sensor("kafka.requests");
        sensor.add(new MetricName("pack.bean1.avg", "grp1"), new Avg());

        Map<String, Gauge> gauges = SharedMetricRegistries.getOrCreate("default").getGauges();
        String expectedName = "org.apache.kafka.common.metrics.grp1.pack.bean1.avg";
        Assert.assertEquals(1, gauges.size());
        Assert.assertEquals(expectedName, gauges.keySet().toArray()[0]);

        sensor.record(2.1);
        sensor.record(2.2);
        sensor.record(2.6);
        Assert.assertEquals(2.3, (Double)gauges.get(expectedName).getValue(), 0.001);
    }
}
