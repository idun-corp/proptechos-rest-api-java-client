package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ACTUATION_INTERFACE_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.actuation.ActuationInterface;
import java.util.UUID;

public class ActuationInterfaceService extends PagedService<ActuationInterface> {

  ActuationInterfaceService(String baseAppUrl) {
    super(baseAppUrl, ACTUATION_INTERFACE_JSON, ActuationInterface.class);
  }

  public ActuationInterface getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(typeClazz, ACTUATION_INTERFACE_JSON, id);
  }

  public ActuationInterface createActuationInterface(
      ActuationInterface actuationInterface) throws ProptechOsServiceException {
    return httpClient.createObject(typeClazz, ACTUATION_INTERFACE_JSON, actuationInterface);
  }

  public ActuationInterface updateActuationInterface(
      ActuationInterface actuationInterface) throws ProptechOsServiceException {
    return httpClient.updateObject(typeClazz, ACTUATION_INTERFACE_JSON, actuationInterface);
  }

  public void deleteActuationInterface(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(ACTUATION_INTERFACE_JSON, id);
  }
}
