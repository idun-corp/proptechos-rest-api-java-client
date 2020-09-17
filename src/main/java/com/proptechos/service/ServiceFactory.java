package com.proptechos.service;

public class ServiceFactory {

  private String baseAppUrl;

  public ServiceFactory(String baseAppUrl) {
    this.baseAppUrl = baseAppUrl;
  }

  public DeviceService deviceService() {
    return new DeviceService(baseAppUrl);
  }
}
