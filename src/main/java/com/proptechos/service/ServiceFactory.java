package com.proptechos.service;

import com.proptechos.model.auth.KafkaConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactory {

  private final String baseAppUrl;
  private final Map<String, Object> services;

  public ServiceFactory(String baseAppUrl) {
    this.baseAppUrl = baseAppUrl;
    this.services = new ConcurrentHashMap<>();
  }

  public DeviceService deviceService() {
    return (DeviceService) this.services.computeIfAbsent(
        DeviceService.class.getSimpleName(), k -> new DeviceService(baseAppUrl));
  }

  public StreamingApiService streamingApiService(KafkaConfig kafkaConfig) {
    return (StreamingApiService) this.services.computeIfAbsent(
        StreamingApiService.class.getSimpleName(), k -> new StreamingApiService(kafkaConfig));
  }
}
