package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Actuator;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.Observation;
import com.proptechos.model.actuation.ActuationRequest;
import com.proptechos.model.actuation.ActuationRequestResponse;
import com.proptechos.model.actuation.result.ActuationResult;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.*;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

public class ActuatorService extends PagedService<Actuator> {

    private final String clientId;

    ActuatorService(String baseAppUrl, String clientId) {
        super(baseAppUrl, ACTUATOR_JSON, Actuator.class);
        this.clientId = clientId;
    }

    public Actuator getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(typeClazz, ACTUATOR_JSON, id);
    }

    public Actuator createActuator(Actuator actuator) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, ACTUATOR_JSON, actuator);
    }

    public Actuator updateActuator(Actuator actuator) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, ACTUATOR_JSON, actuator);
    }

    public void deleteActuator(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(ACTUATOR_JSON, id);
    }

    public BatchResponse<Actuator> createActuators(List<Actuator> actuators) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, ACTUATORS_JSON, actuators);
    }

    public BatchResponse<Actuator> updateActuators(List<Actuator> actuators) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, ACTUATORS_JSON, actuators);
    }

    public ActuationRequestResponse sendActuationRequest(UUID actuatorId, String payload) {
        return sendActuationRequest(actuatorId, null, payload);
    }

    public ActuationResult getActuationResult(UUID actuatorId, UUID actuationCommandId) {
        String getActuationUri = String.format(ACTUATION_JSON, actuatorId) + "/" + actuationCommandId;
        return httpClient.getSingle(ActuationResult.class, getActuationUri);
    }

    public Observation getActuationLastValue(UUID actuatorId) {
        String getActuationUri = String.format(ACTUATOR_LAST_VALUE_JSON, actuatorId);
        return httpClient.getSingle(Observation.class, getActuationUri);
    }

    public List<Observation> getObservationsBySensorIdForPeriod(
            UUID actuatorId, Instant startTime, Instant endTime) {
        return httpClient.getList(Observation.class, String.format(ACTUATOR_OBSERVATIONS_JSON, actuatorId),
                StartTimeFilter.getInstance(startTime),
                EndTimeFilter.getInstance(endTime));
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

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(ACTUATOR_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

}
