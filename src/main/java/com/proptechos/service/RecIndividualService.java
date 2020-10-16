package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.BUILDING_COMPONENT_CLASS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.DEVICE_FUNCTION_JSON;
import static com.proptechos.http.constants.ApiEndpoints.MEASUREMENT_UNIT_JSON;
import static com.proptechos.http.constants.ApiEndpoints.PLACEMENT_CONTEXT_JSON;
import static com.proptechos.http.constants.ApiEndpoints.QUANTITY_KIND_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ROOM_TYPE_JSON;

import com.proptechos.http.JsonHttpClient;
import com.proptechos.model.definitions.BuildingComponentClass;
import com.proptechos.model.definitions.DeviceFunctionType;
import com.proptechos.model.definitions.MeasurementUnit;
import com.proptechos.model.definitions.PlacementContext;
import com.proptechos.model.definitions.QuantityKind;
import com.proptechos.model.definitions.RoomType;
import java.util.List;

public class RecIndividualService {

  private final JsonHttpClient httpClient;

  RecIndividualService(String baseAppUrl) {
    this.httpClient = JsonHttpClient.getInstance(baseAppUrl);
  }

  public List<DeviceFunctionType> getDeviceFunctionTypes() {
    return httpClient.getList(DeviceFunctionType.class, DEVICE_FUNCTION_JSON);
  }

  public List<MeasurementUnit> getMeasurementUnits() {
    return httpClient.getList(MeasurementUnit.class, MEASUREMENT_UNIT_JSON);
  }

  public List<PlacementContext> getPlacementContexts() {
    return httpClient.getList(PlacementContext.class, PLACEMENT_CONTEXT_JSON);
  }

  public List<QuantityKind> getQuantityKinds() {
    return httpClient.getList(QuantityKind.class, QUANTITY_KIND_JSON);
  }

  public List<RoomType> getRoomTypes() {
    return httpClient.getList(RoomType.class, ROOM_TYPE_JSON);
  }

  public List<BuildingComponentClass> getBuildingComponentClasses() {
    return httpClient.getList(BuildingComponentClass.class, BUILDING_COMPONENT_CLASS_JSON);
  }

}
