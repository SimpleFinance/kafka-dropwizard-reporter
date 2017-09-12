package com.simple.metrics.kafka;

import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteSender;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DropwizardReporterGraphite extends DropwizardReporter {
  private static final Logger LOGGER = LoggerFactory.getLogger(DropwizardReporterGraphite.class);

  GraphiteSender graphite = null;
  GraphiteReporter reporter = null;

  @Override
  public void init(List<KafkaMetric> list) {
    super.init(list);
    InetSocketAddress address = new InetSocketAddress(
        config.getString(DropwizardReporterConfig.GRAPHITE_HOST_PROPERTY_NAME),
        config.getInt(DropwizardReporterConfig.GRAPHITE_PORT_PROPERTY_NAME));
    graphite = new Graphite(address);
    reporter = GraphiteReporter.forRegistry(registry)
        .prefixedWith(config.getString(DropwizardReporterConfig.GRAPHITE_PREFIX_PROPERTY_NAME))
        .build(graphite);
    LOGGER.info("Starting the reporter");
    reporter.start(11, TimeUnit.SECONDS);
  }

  @Override
  public void close() {
    LOGGER.info("Stopping and closing the reporter");
    reporter.stop();
    reporter.close();
    super.close();
  }
}
