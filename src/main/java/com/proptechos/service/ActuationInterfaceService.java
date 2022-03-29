package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ACTUATION_INTERFACE_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.actuation.ActuationInterface;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.List;
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

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(ACTUATION_INTERFACE_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }
}
