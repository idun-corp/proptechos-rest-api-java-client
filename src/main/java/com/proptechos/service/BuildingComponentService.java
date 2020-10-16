package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.BUILDING_COMPONENT_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.common.IBuildingComponent;
import java.util.UUID;

public class BuildingComponentService extends PagedService<IBuildingComponent> {

  BuildingComponentService(String baseAppUrl) {
    super(baseAppUrl, BUILDING_COMPONENT_JSON);
  }

  public IBuildingComponent getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(IBuildingComponent.class, BUILDING_COMPONENT_JSON, id);
  }

  public IBuildingComponent createBuildingComponent(IBuildingComponent buildingComponent) throws ProptechOsServiceException {
    return httpClient.createObject(IBuildingComponent.class, BUILDING_COMPONENT_JSON, buildingComponent);
  }

  public IBuildingComponent updateBuildingComponent(IBuildingComponent buildingComponent) throws ProptechOsServiceException {
    return httpClient.updateObject(IBuildingComponent.class, BUILDING_COMPONENT_JSON, buildingComponent);
  }

  public void deleteBuildingComponent(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(BUILDING_COMPONENT_JSON, id);
  }

}
