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
   * @return RealEstateService instance
   *
   * @see com.proptechos.service.RealEstateService
   */
  public RealEstateService realEstateService() {
    return (RealEstateService) this.services.computeIfAbsent(
        RealEstateService.class.getSimpleName(), k -> new RealEstateService(baseAppUrl));
  }

  /**
   * @return RealEstateComponentService instance
   *
   * @see com.proptechos.service.RealEstateComponentService
   */
  public RealEstateComponentService realEstateComponentService() {
    return (RealEstateComponentService) this.services.computeIfAbsent(
        RealEstateComponentService.class.getSimpleName(), k -> new RealEstateComponentService(baseAppUrl));
  }

  /**
   * @return BuildingComponentService instance
   *
   * @see com.proptechos.service.BuildingComponentService
   */
  public BuildingComponentService buildingComponentService() {
    return (BuildingComponentService) this.services.computeIfAbsent(
        BuildingComponentService.class.getSimpleName(), k -> new BuildingComponentService(baseAppUrl));
  }

  /**
   * @return RoomService instance
   *
   * @see com.proptechos.service.RoomService
   */
  public RoomService roomService() {
    return (RoomService) this.services.computeIfAbsent(
        RoomService.class.getSimpleName(), k -> new RoomService(baseAppUrl));
  }

  /**
   * @return StoreyService instance
   *
   * @see com.proptechos.service.StoreyService
   */
  public StoreyService storeyService() {
    return (StoreyService) this.services.computeIfAbsent(
        StoreyService.class.getSimpleName(), k -> new StoreyService(baseAppUrl));
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
   * @return ActuatorService instance
   *
   * @see com.proptechos.service.ActuatorService
   */
  public ActuatorService actuatorService() {
    return (ActuatorService) this.services.computeIfAbsent(
        ActuatorService.class.getSimpleName(), k -> new ActuatorService(baseAppUrl));
  }

  /**
   * @return SensorService instance
   *
   * @see com.proptechos.service.SensorService
   */
  public SensorService sensorService() {
    return (SensorService) this.services.computeIfAbsent(
        SensorService.class.getSimpleName(), k -> new SensorService(baseAppUrl));
  }

  /**
   * @return ActuationInterfaceService instance
   *
   * @see com.proptechos.service.ActuationInterfaceService
   */
  public ActuationInterfaceService actuationInterfaceService() {
    return (ActuationInterfaceService) this.services.computeIfAbsent(
        ActuationInterfaceService.class.getSimpleName(), k -> new ActuationInterfaceService(baseAppUrl));
  }

  /**
   * @return AliasNamespaceService instance
   *
   * @see com.proptechos.service.AliasNamespaceService
   */
  public AliasNamespaceService aliasNamespaceService() {
    return (AliasNamespaceService) this.services.computeIfAbsent(
        AliasNamespaceService.class.getSimpleName(), k -> new AliasNamespaceService(baseAppUrl));
  }

  /**
   * @return RecIndividualService instance
   *
   * @see com.proptechos.service.RecIndividualService
   */
  public RecIndividualService recIndividualService() {
    return (RecIndividualService) this.services.computeIfAbsent(
        RecIndividualService.class.getSimpleName(), k -> new RecIndividualService(baseAppUrl));
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
