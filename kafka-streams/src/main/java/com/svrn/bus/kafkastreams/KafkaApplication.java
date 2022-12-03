package com.databus.kafkastreams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class KafkaApplication {

    private static final String inputTopic = "data_topic";
    private static final String outputTopic = "data_analysis_topic";

    private static Logger logger = LoggerFactory.getLogger(KafkaApplication.class);

    public static void main(final String[] args) throws Exception {
        final String bootstrapServers = "kafka-1:9092";

        final Properties streamsConfiguration = getStreamsConfiguration(bootstrapServers);

        final StreamsBuilder builder = new StreamsBuilder();
        createWordCountStream(builder);
        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);

        logger.info(String.format("Preparing kafka streams"));
        streams.cleanUp();
        streams.start();
        logger.info(String.format("Kafka streams started"));

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static Properties getStreamsConfiguration(final String bootstrapServers) throws Exception {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-stream");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "kafka-stream-client");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        return streamsConfiguration;
    }

    private static void createWordCountStream(final StreamsBuilder builder) {
        final KStream<String, String> textLines = builder.stream(inputTopic);

        final KTable<String, Long> wordCounts = textLines
                .mapValues(value -> parseJson(value))
                .flatMapValues(value -> Arrays.asList(value.getData().toLowerCase().split(" ")))
                .groupBy((key, word) -> word)
                .count();

        wordCounts.toStream()
                .map((key, value) -> {
                    logger.info(String.format("Sending message => %s , count: %d", key, value));
                    return KeyValue.pair("Word: " + key, "Count: " + value.toString());
                })
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));
    }

    private static Data parseJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Data.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error when parsing json", e);
        }
    }
}