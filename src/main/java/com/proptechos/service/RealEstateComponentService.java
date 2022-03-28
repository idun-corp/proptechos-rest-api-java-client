package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.Building;
import com.proptechos.model.Point;
import com.proptechos.model.common.IRealEstateComponent;
import com.proptechos.service.filters.location.DistanceFilter;
import com.proptechos.service.filters.location.LatitudeFilter;
import com.proptechos.service.filters.location.LongitudeFilter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_COMPONENTS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_COMPONENT_JSON;

public class RealEstateComponentService extends PagedService<IRealEstateComponent> {

    RealEstateComponentService(String baseAppUrl) {
        super(baseAppUrl, REAL_ESTATE_COMPONENT_JSON, IRealEstateComponent.class);
    }

    public Building getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(Building.class, REAL_ESTATE_COMPONENT_JSON, id);
    }

    public List<IRealEstateComponent> getRealEstateComponentsInRange(Point point) {
        return httpClient.getList(typeClazz, REAL_ESTATE_COMPONENT_JSON + "/inrange",
            new LatitudeFilter(point.getLatitude()),
            new LongitudeFilter(point.getLongitude()),
            new DistanceFilter(point.getDistance()));
    }

    public IRealEstateComponent createRealEstateComponent(IRealEstateComponent realEstateComponent) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, REAL_ESTATE_COMPONENT_JSON, realEstateComponent);
    }

    public IRealEstateComponent updateRealEstateComponent(IRealEstateComponent realEstateComponent) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, REAL_ESTATE_COMPONENT_JSON, realEstateComponent);
    }

    public void deleteRealEstateComponent(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(REAL_ESTATE_COMPONENT_JSON, id);
    }

    public BatchResponse<IRealEstateComponent> createRealEstateComponents(List<IRealEstateComponent> realEstateComponents) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, REAL_ESTATE_COMPONENTS_JSON, realEstateComponents);
    }

    public BatchResponse<IRealEstateComponent> updateRealEstateComponents(List<IRealEstateComponent> realEstateComponents) throws ProptechOsServiceException {
        return httpClient.updateBatch(IRealEstateComponent.class, REAL_ESTATE_COMPONENTS_JSON, realEstateComponents);
    }

    @Deprecated
    public List<IRealEstateComponent> getBuildingsInRange(Point point) {
        return httpClient.getList(typeClazz, REAL_ESTATE_COMPONENT_JSON + "/inrange",
            new LatitudeFilter(point.getLatitude()),
            new LongitudeFilter(point.getLongitude()),
            new DistanceFilter(point.getDistance()))
            .stream().filter(rc -> rc instanceof Building).collect(Collectors.toList());
    }

    @Deprecated
    public Building createBuilding(Building building) throws ProptechOsServiceException {
        return httpClient.createObject(Building.class, REAL_ESTATE_COMPONENT_JSON, building);
    }

    @Deprecated
    public Building updateBuilding(Building building) throws ProptechOsServiceException {
        return httpClient.updateObject(Building.class, REAL_ESTATE_COMPONENT_JSON, building);
    }

    @Deprecated
    public void deleteBuilding(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(REAL_ESTATE_COMPONENT_JSON, id);
    }

    @Deprecated
    public BatchResponse<Building> createBuildings(List<Building> buildings) throws ProptechOsServiceException {
        return httpClient.createBatch(Building.class, REAL_ESTATE_COMPONENTS_JSON, buildings);
    }

    @Deprecated
    public BatchResponse<Building> updateBuildings(List<Building> buildings) throws ProptechOsServiceException {
        return httpClient.updateBatch(Building.class, REAL_ESTATE_COMPONENTS_JSON, buildings);
    }

}
