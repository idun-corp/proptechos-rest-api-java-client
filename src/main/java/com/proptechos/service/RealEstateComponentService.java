package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_COMPONENT_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Building;
import com.proptechos.model.Point;
import com.proptechos.service.filters.DistanceFilter;
import com.proptechos.service.filters.LatitudeFilter;
import com.proptechos.service.filters.LongitudeFilter;
import java.util.List;
import java.util.UUID;

public class RealEstateComponentService extends PagedService<Building> {

  RealEstateComponentService(String baseAppUrl) {
    super(baseAppUrl, REAL_ESTATE_COMPONENT_JSON);
  }

  public Building getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(Building.class, REAL_ESTATE_COMPONENT_JSON, id);
  }

  public List<Building> getBuildingsInRange(Point point) {
    return httpClient.getList(Building.class, REAL_ESTATE_COMPONENT_JSON + "/inrange",
        new LatitudeFilter(point.getLatitude()),
        new LongitudeFilter(point.getLongitude()),
        new DistanceFilter(point.getDistance()));
  }

  public Building createBuilding(Building building) throws ProptechOsServiceException {
    return httpClient.createObject(Building.class, REAL_ESTATE_COMPONENT_JSON, building);
  }

  public Building updateBuilding(Building building) throws ProptechOsServiceException {
    return httpClient.updateObject(Building.class, REAL_ESTATE_COMPONENT_JSON, building);
  }

  public void deleteBuilding(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(REAL_ESTATE_COMPONENT_JSON, id);
  }

}
