package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.buildingcomponent.Storey;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.STOREYS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.STOREY_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

public class StoreyService extends PagedService<Storey> {

    StoreyService(String baseAppUrl) {
        super(baseAppUrl, STOREY_JSON, Storey.class);
    }

    public Storey getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(typeClazz, STOREY_JSON, id);
    }

    public Storey createStorey(Storey storey) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, STOREY_JSON, storey);
    }

    public Storey updateStorey(Storey storey) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, STOREY_JSON, storey);
    }

    public void deleteStorey(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(STOREY_JSON, id);
    }

    public BatchResponse<Storey> createStoreys(List<Storey> storeys) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, STOREYS_JSON, storeys);
    }

    public BatchResponse<Storey> updateStoreys(List<Storey> storeys) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, STOREYS_JSON, storeys);
    }

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(STOREY_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

}
