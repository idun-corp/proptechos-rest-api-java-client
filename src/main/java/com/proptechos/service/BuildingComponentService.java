package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.common.IBuildingComponent;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.BUILDING_COMPONENTS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.BUILDING_COMPONENT_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

public class BuildingComponentService extends PagedService<IBuildingComponent> {

    BuildingComponentService(String baseAppUrl) {
        super(baseAppUrl, BUILDING_COMPONENT_JSON, IBuildingComponent.class);
    }

    public IBuildingComponent getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(typeClazz, BUILDING_COMPONENT_JSON, id);
    }

    public IBuildingComponent createBuildingComponent(IBuildingComponent buildingComponent) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, BUILDING_COMPONENT_JSON, buildingComponent);
    }

    public IBuildingComponent updateBuildingComponent(IBuildingComponent buildingComponent) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, BUILDING_COMPONENT_JSON, buildingComponent);
    }

    public void deleteBuildingComponent(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(BUILDING_COMPONENT_JSON, id);
    }

    public BatchResponse<IBuildingComponent> createBuildingComponents(List<IBuildingComponent> buildingComponents) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, BUILDING_COMPONENTS_JSON, buildingComponents);
    }

    public BatchResponse<IBuildingComponent> updateBuildingComponents(List<IBuildingComponent> buildingComponents) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, BUILDING_COMPONENTS_JSON, buildingComponents);
    }

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(BUILDING_COMPONENT_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

}
