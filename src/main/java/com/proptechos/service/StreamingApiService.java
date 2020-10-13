package com.proptechos.service;

import com.proptechos.model.Observation;
import com.proptechos.model.auth.KafkaConfig;
import java.util.function.Consumer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

/**
 * StreamingApiService main entry class to connect to Azure eventhub to listen to Streaming API
 *
 * @apiNote Subscription could be done once, otherwise subscribers will compete for the single topic
 */
public class StreamingApiService {

  private final KafkaConfig kafkaConfig;
  private final StreamsBuilder builder;

  StreamingApiService(KafkaConfig kafkaConfig) {
    this.kafkaConfig = kafkaConfig;
    this.builder = new StreamsBuilder();
  }

  public KafkaStreams subscribe(Consumer<String> onNext) {
    KStream<String, String> telemetry = builder.stream(kafkaConfig.getTopic());
    telemetry.foreach((key, observation) -> onNext.accept(observation));
    KafkaStreams streams = new KafkaStreams(builder.build(), kafkaConfig.getConfigMap());
    streams.start();
    return streams;
  }

}
