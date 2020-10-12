package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ACTUATION_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ACTUATOR_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Actuator;
import com.proptechos.model.actuation.ActuationRequest;
import com.proptechos.model.actuation.ActuationRequestResponse;
import java.util.UUID;

public class ActuatorService extends PagedService<Actuator> {

  private final String clientId;
  
  ActuatorService(String baseAppUrl, String clientId) {
    super(baseAppUrl, ACTUATOR_JSON);
    this.clientId = clientId;
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

  public ActuationRequestResponse sendActuationRequest(UUID actuatorId, String payload) {
    return sendActuationRequest(actuatorId, null, payload);
  }

  public ActuationRequestResponse sendActuationRequest(
      UUID actuatorId, UUID targetInterfaceId, String payload) {
    ActuationRequest request = new ActuationRequest();
    request.setPayload(payload);
    request.setRequestingAgent(clientId);
    request.setTargetInterface(targetInterfaceId);
    return httpClient.updateObject(
        ActuationRequestResponse.class, String.format(ACTUATION_JSON, actuatorId), request);
  }

}
