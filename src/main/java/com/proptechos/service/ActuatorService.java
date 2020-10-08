package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ACTUATOR_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Actuator;
import java.util.UUID;

public class ActuatorService extends PagedService<Actuator> {
  
  ActuatorService(String baseAppUrl) {
    super(baseAppUrl, ACTUATOR_JSON);
  }

  public Actuator getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(Actuator.class, ACTUATOR_JSON, id);
  }

  public Actuator createActuator(Actuator actuator) throws ProptechOsServiceException {
    return httpClient.createObject(Actuator.class, ACTUATOR_JSON, actuator);
  }

  public Actuator updateActuator(Actuator actuator) throws ProptechOsServiceException {
    return httpClient.updateObject(Actuator.class, ACTUATOR_JSON, actuator);
  }

  public void deleteActuator(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(ACTUATOR_JSON, id);
  }

}
