package com.proptechos.service;

import com.proptechos.utils.DataLoader;
import com.proptechos.utils.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

import static com.proptechos.http.constants.ApiEndpoints.*;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;

@ExtendWith(WireMockExtension.class)
public class RecIndividualServiceTest extends BaseServiceTest {

    private static RecIndividualService recIndividualService;

    @BeforeAll
    static void setUp() {
        recIndividualService = serviceFactory.recIndividualService();
    }

    @Test
    void testGetBuildingComponentClasses() {
        stubGetResponse(BUILDING_COMPONENT_CLASS_JSON,
            new HashMap<>(), DataLoader.loadBuildingComponentClasses());

        recIndividualService.getBuildingComponentClasses();

        verifyGetRequest(BUILDING_COMPONENT_CLASS_JSON, new HashMap<>());
    }

    @Test
    void testGetRoomTypes() {
        stubGetResponse(ROOM_TYPE_JSON, new HashMap<>(), DataLoader.loadRoomTypes());

        recIndividualService.getRoomTypes();

        verifyGetRequest(ROOM_TYPE_JSON, new HashMap<>());
    }

    @Test
    void testGetDeviceFunctionTypes() {
        stubGetResponse(DEVICE_FUNCTION_JSON, new HashMap<>(), DataLoader.loadDeviceFunctions());

        recIndividualService.getDeviceFunctionTypes();

        verifyGetRequest(DEVICE_FUNCTION_JSON, new HashMap<>());
    }

    @Test
    void testGetMeasurementUnits() {
        stubGetResponse(MEASUREMENT_UNIT_JSON, new HashMap<>(), DataLoader.loadMeasurementUnits());

        recIndividualService.getMeasurementUnits();

        verifyGetRequest(MEASUREMENT_UNIT_JSON, new HashMap<>());
    }

    @Test
    void testGetPlacementContexts() {
        stubGetResponse(PLACEMENT_CONTEXT_JSON, new HashMap<>(), DataLoader.loadPlacementContexts());

        recIndividualService.getPlacementContexts();

        verifyGetRequest(PLACEMENT_CONTEXT_JSON, new HashMap<>());
    }

    @Test
    void testGetQuantityKinds() {
        stubGetResponse(QUANTITY_KIND_JSON, new HashMap<>(), DataLoader.loadQuantityKinds());

        recIndividualService.getQuantityKinds();

        verifyGetRequest(QUANTITY_KIND_JSON, new HashMap<>());
    }

}
