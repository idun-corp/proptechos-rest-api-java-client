package com.proptechos.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactory {

  private static volatile ServiceFactory INSTANCE;
  private static final Object lock = new Object();

  private final String baseAppUrl;
  private final Map<String, Object> services;

  public static ServiceFactory getInstance(String baseAppUrl) {
    if(INSTANCE == null) {
      synchronized(lock) {
        if(INSTANCE == null) {
          INSTANCE = new ServiceFactory(baseAppUrl);
        }
      }
    }

    return INSTANCE;
  }

  private ServiceFactory(String baseAppUrl) {
    this.baseAppUrl = baseAppUrl;
    this.services = new ConcurrentHashMap<>();
  }

  public DeviceService deviceService() {
    return (DeviceService)this.services.computeIfAbsent(
        DeviceService.class.getSimpleName(), k -> new DeviceService(baseAppUrl));
  }
}
