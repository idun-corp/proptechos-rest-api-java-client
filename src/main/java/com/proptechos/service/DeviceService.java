package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.DEVICE_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.http.JsonHttpClient;
import com.proptechos.model.Device;
import java.util.UUID;

public class DeviceService {

  private final JsonHttpClient<Device> httpClient;

  public DeviceService(String baseAppUrl) {
    this.httpClient = new JsonHttpClient<>(baseAppUrl);
  }

  public Device getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(Device.class, DEVICE_JSON, id);
  }

  public Device createDevice(Device device) throws ProptechOsServiceException {
    return httpClient.createObject(Device.class, DEVICE_JSON, device);
  }

  public Device updateDevice(Device device) throws ProptechOsServiceException {
    return httpClient.updateObject(Device.class, DEVICE_JSON, device);
  }

}
