package com.simple.example;

import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Properties;

public class DropwizardReporterExampleConfiguration extends Configuration {
    @NotEmpty
    private Properties producer;

    @NotEmpty
    private Properties consumer;

    public Properties getProducer() {
        return producer;
    }

    public void setProducer(Properties producer) {
        this.producer = producer;
    }

    public Properties getConsumer() {
        return consumer;
    }

    public void setConsumer(Properties consumer) {
        this.consumer = consumer;
    }
}
