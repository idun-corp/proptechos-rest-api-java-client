package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ACTUATION_INTERFACE_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.actuation.ActuationInterface;
import java.util.UUID;

public class ActuationInterfaceService extends PagedService<ActuationInterface> {

  ActuationInterfaceService(String baseAppUrl) {
    super(baseAppUrl, ACTUATION_INTERFACE_JSON);
  }

  public ActuationInterface getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(ActuationInterface.class, ACTUATION_INTERFACE_JSON, id);
  }
}
