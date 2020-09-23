package com.proptechos.service;

import com.proptechos.model.Observation;
import com.proptechos.model.auth.KafkaConfig;
import com.proptechos.model.auth.KafkaRetryConfig;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamingApiService {

  private final Logger log = LoggerFactory.getLogger(StreamingApiService.class);

  private final KafkaConsumer<String, Observation> consumer;
  private final KafkaRetryConfig retryConfig;

  StreamingApiService(KafkaConfig kafkaConfig) {
    this.consumer = new KafkaConsumer<>(kafkaConfig.getConfigMap());
    this.consumer.subscribe(Collections.singletonList(kafkaConfig.getTopic()));
    this.retryConfig = kafkaConfig.retryConfig();
  }

  private Observable<ConsumerRecord<String, Observation>> pollObservations() {
    return Observable.interval(0,
        retryConfig.getRetryInterval() , retryConfig.getTimeUnit())
        .flatMapIterable(i -> consumer.poll(Duration.ofSeconds(i)))
        .retryWhen(errors -> {
          AtomicInteger count = new AtomicInteger();
          return errors
              .takeWhile(e -> count.getAndIncrement() != retryConfig.getRetryNumber())
              .flatMap(e -> {
                log.debug("Delay retry by " + count.get() + " second(s)");
                return Observable.timer(count.get(), TimeUnit.SECONDS);
              });
        });
  }

  public void subscribe(Consumer<Observation> onNext) {
    subscribe(onNext, throwable -> log.error(throwable.getMessage(), throwable));
  }

  public void blockingSubscribe(Consumer<Observation> onNext) {
    blockingSubscribe(onNext, throwable -> log.error(throwable.getMessage(), throwable));
  }

  public void subscribe(Consumer<Observation> onNext, Consumer<? super Throwable> onError) {
    pollObservations().map(ConsumerRecord::value).blockingSubscribe(onNext, onError);
  }

  public void blockingSubscribe(Consumer<Observation> onNext, Consumer<? super Throwable> onError) {
    pollObservations().map(ConsumerRecord::value).blockingSubscribe(onNext, onError);
  }

}
