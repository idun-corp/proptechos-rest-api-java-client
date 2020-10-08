package com.proptechos.service;

import com.proptechos.model.Observation;
import com.proptechos.model.auth.KafkaConfig;
import com.proptechos.model.auth.KafkaRetryConfig;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StreamingApiService main entry class to connect to Azure eventhub to listen to Streaming API
 *
 * @apiNote Subscription could be done once, otherwise subscribers will compete for the single topic
 */
public class StreamingApiService {

  private final Logger log = LoggerFactory.getLogger(StreamingApiService.class);

  private final KafkaConsumer<String, Observation> consumer;
  private final KafkaRetryConfig retryConfig;
  private Disposable subscription;

  private final KafkaConfig kafkaConfig;
  private final KafkaStreams streams;
  private final StreamsBuilder builder;

  StreamingApiService(KafkaConfig kafkaConfig) {
    this.consumer = new KafkaConsumer<>(kafkaConfig.getConfigMap());
    this.consumer.subscribe(Collections.singletonList(kafkaConfig.getTopic()));
    this.retryConfig = kafkaConfig.retryConfig();

    this.builder = new StreamsBuilder();
    this.kafkaConfig = kafkaConfig;
    streams = new KafkaStreams(builder.build(), kafkaConfig.getStreamConfigMap());
  }

  private Observable<ConsumerRecord<String, Observation>> pollObservations() {
    return Observable.interval(0,
        retryConfig.getInterval() , retryConfig.getTimeUnit())
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

//  private Observable<ConsumerRecord<String, Observation>> pollObservations2() {
//    Flowable<String> o = Flowable.generate(
//        () -> builder.stream(kafkaConfig.getTopic()),
//        (stream, output) -> {
//          try {
//            stream.foreach((o1, o2) -> {
//              output.onNext(o2.toString());
//            });
////            output.onNext(stream.);
////              output.onComplete();
//
//          } catch (Exception ex) {
//            output.onError(ex);
//          }
//          return stream;
//        },
//        stream -> {
//
//        }
//    );
//  }

  public void subscribe(Consumer<Observation> onNext) {
    subscribe(onNext, throwable -> log.error(throwable.getMessage(), throwable));
  }

  public void blockingSubscribe(Consumer<Observation> onNext) {
    blockingSubscribe(onNext, throwable -> log.error(throwable.getMessage(), throwable));
  }

  public void subscribe(Consumer<Observation> onNext, Consumer<? super Throwable> onError) {
    subscription = pollObservations().map(ConsumerRecord::value).subscribe(onNext, onError);
  }

  public void blockingSubscribe(Consumer<Observation> onNext, Consumer<? super Throwable> onError) {
    pollObservations().map(ConsumerRecord::value).blockingSubscribe(onNext, onError);
  }

//  public void blockingSubscribeOnStream(Consumer<Observation> onNext, Consumer<? super Throwable> onError) {
//    streams.start();
//    pollObservations().map(ConsumerRecord::value).blockingSubscribe(onNext, onError);
//  }

  public void unsubscribe() {
    subscription.dispose();
  }

}