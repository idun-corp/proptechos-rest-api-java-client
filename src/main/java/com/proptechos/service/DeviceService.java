package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.DEVICE_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.common.IDevice;
import java.util.UUID;

public class DeviceService extends PagedService<IDevice> {

  DeviceService(String baseAppUrl) {
    super(baseAppUrl, DEVICE_JSON, IDevice.class);
  }

  public IDevice getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(typeClazz, DEVICE_JSON, id);
  }

  public IDevice createDevice(IDevice device) throws ProptechOsServiceException {
    return httpClient.createObject(typeClazz, DEVICE_JSON, device);
  }

  public IDevice updateDevice(IDevice device) throws ProptechOsServiceException {
    return httpClient.updateObject(typeClazz, DEVICE_JSON, device);
  }

  public void deleteDevice(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(DEVICE_JSON, id);
  }

}
