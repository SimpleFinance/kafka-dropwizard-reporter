generate-config-docs:
	mvn exec:java -Dexec.mainClass=com.simple.metrics.kafka.DropwizardReporterConfig -Dexec.classpathScope="compile"
