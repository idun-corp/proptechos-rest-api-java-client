package com.proptechos.service;

import com.proptechos.model.auth.KafkaConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceFactory class for obtaining all services to work with ProptechOS API
 */
public class ServiceFactory {

  private final String baseAppUrl;
  private final Map<String, Object> services;

  public ServiceFactory(String baseAppUrl) {
    this.baseAppUrl = baseAppUrl;
    this.services = new ConcurrentHashMap<>();
  }

  /**
   * @return DeviceService instance
   *
   * @see com.proptechos.service.DeviceService
   */
  public DeviceService deviceService() {
    return (DeviceService) this.services.computeIfAbsent(
        DeviceService.class.getSimpleName(), k -> new DeviceService(baseAppUrl));
  }

  /**
   * @param kafkaConfig configuration data to connect to kafka stream
   * @return StreamingApiService instance
   *
   * @see com.proptechos.service.StreamingApiService
   * @see com.proptechos.model.auth.KafkaConfig
   */
  public StreamingApiService streamingApiService(KafkaConfig kafkaConfig) {
    return (StreamingApiService) this.services.computeIfAbsent(
        StreamingApiService.class.getSimpleName(), k -> new StreamingApiService(kafkaConfig));
  }
}
