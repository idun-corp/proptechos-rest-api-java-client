package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.BUILDING_COMPONENT_CLASS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.DEVICE_FUNCTION_JSON;
import static com.proptechos.http.constants.ApiEndpoints.MEASUREMENT_UNIT_JSON;
import static com.proptechos.http.constants.ApiEndpoints.PLACEMENT_CONTEXT_JSON;
import static com.proptechos.http.constants.ApiEndpoints.QUANTITY_KIND_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ROOM_TYPE_JSON;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;

import com.proptechos.utils.DataLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {8080})
public class RecIndividualServiceTest extends BaseServiceTest {

  private static RecIndividualService recIndividualService;
  private final MockServerClient client;

  RecIndividualServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    recIndividualService = serviceFactory.recIndividualService();
  }

  @Test
  void testGetBuildingComponentClasses() {
    client.when(getRequest(BUILDING_COMPONENT_CLASS_JSON))
        .respond(okResponse(DataLoader.loadBuildingComponentClasses()));

    recIndividualService.getBuildingComponentClasses();

    verifyGetRequest(client, BUILDING_COMPONENT_CLASS_JSON);
  }

  @Test
  void testGetRoomTypes() {
    client.when(getRequest(ROOM_TYPE_JSON))
        .respond(okResponse(DataLoader.loadRoomTypes()));

    recIndividualService.getRoomTypes();

    verifyGetRequest(client, ROOM_TYPE_JSON);
  }

  @Test
  void testGetDeviceFunctionTypes() {
    client.when(getRequest(DEVICE_FUNCTION_JSON))
        .respond(okResponse(DataLoader.loadDeviceFunctions()));

    recIndividualService.getDeviceFunctionTypes();

    verifyGetRequest(client, DEVICE_FUNCTION_JSON);
  }

  @Test
  void testGetMeasurementUnits() {
    client.when(getRequest(MEASUREMENT_UNIT_JSON))
        .respond(okResponse(DataLoader.loadMeasurementUnits()));

    recIndividualService.getMeasurementUnits();

    verifyGetRequest(client, MEASUREMENT_UNIT_JSON);
  }

  @Test
  void testGetPlacementContexts() {
    client.when(getRequest(PLACEMENT_CONTEXT_JSON))
        .respond(okResponse(DataLoader.loadPlacementContexts()));

    recIndividualService.getPlacementContexts();

    verifyGetRequest(client, PLACEMENT_CONTEXT_JSON);
  }

  @Test
  void testGetQuantityKinds() {
    client.when(getRequest(QUANTITY_KIND_JSON))
        .respond(okResponse(DataLoader.loadQuantityKinds()));

    recIndividualService.getQuantityKinds();

    verifyGetRequest(client, QUANTITY_KIND_JSON);
  }

}
