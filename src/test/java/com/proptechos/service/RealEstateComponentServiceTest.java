package com.proptechos.service;

import com.proptechos.model.Building;
import com.proptechos.model.Land;
import com.proptechos.model.Point;
import com.proptechos.model.common.IRealEstateComponent;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import com.proptechos.utils.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_COMPONENTS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_COMPONENT_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class RealEstateComponentServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedBuildings();
    private static final String LIST_DATA = DataLoader.loadBuildingList();
    private static final String TEST_BUILDING_ID = "79b213c9-57d9-4969-9963-70efd069c0d3";
    private static final Building TEST_BUILDING = buildBuilding();
    private static final Land TEST_LAND = buildLand();

    private static RealEstateComponentService realEstateComponentService;

    @BeforeAll
    static void setUp() {
        realEstateComponentService = serviceFactory.realEstateComponentService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "15");

        stubGetResponse(REAL_ESTATE_COMPONENT_JSON, parameters, PAGED_DATA);

        realEstateComponentService.getFirstPage(15);

        verifyGetRequest(REAL_ESTATE_COMPONENT_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "3");
        parameters2.put("size", "15");

        stubGetResponse(REAL_ESTATE_COMPONENT_JSON, parameters1, PAGED_DATA);
        stubGetResponse(REAL_ESTATE_COMPONENT_JSON, parameters2, PAGED_DATA);

        realEstateComponentService.getLastPage(15);

        verifyGetRequest(REAL_ESTATE_COMPONENT_JSON, parameters1);
        verifyGetRequest(REAL_ESTATE_COMPONENT_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "15");

        stubGetResponse(REAL_ESTATE_COMPONENT_JSON, parameters, PAGED_DATA);

        realEstateComponentService.getPage(1, 15);

        verifyGetRequest(REAL_ESTATE_COMPONENT_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "15");

        stubGetResponse(REAL_ESTATE_COMPONENT_JSON, parameters1, PAGED_DATA);
        stubGetResponse(REAL_ESTATE_COMPONENT_JSON, parameters2, PAGED_DATA);

        Paged<IRealEstateComponent> paged = realEstateComponentService.getFirstPage(15);
        realEstateComponentService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(REAL_ESTATE_COMPONENT_JSON, parameters1);
        verifyGetRequest(REAL_ESTATE_COMPONENT_JSON, parameters2);
    }

    @Test
    void testGetInRange() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("lat", "59.0");
        parameters.put("lon", "18.0");
        parameters.put("dist", "100.0");

        stubGetResponse(REAL_ESTATE_COMPONENT_JSON + "/inrange", parameters, LIST_DATA);

        realEstateComponentService
            .getRealEstateComponentsInRange(new Point(59, 18, 100));

        verifyGetRequest(REAL_ESTATE_COMPONENT_JSON + "/inrange", parameters);
    }

    @Test
    void testGetById() {
        stubGetResponse(REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID,
            new HashMap<>(), DataLoader.loadSingleBuilding());

        realEstateComponentService.getById(UUID.fromString(TEST_BUILDING_ID));

        verifyGetRequest(REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID, new HashMap<>());
    }

    @Test
    void testCreateBuilding() {
        stubPostResponse(REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_BUILDING));

        realEstateComponentService.createRealEstateComponent(TEST_BUILDING);

        verifyPostRequest(REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_BUILDING));
    }

    @Test
    void testUpdateBuilding() {
        stubPutResponse(REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_BUILDING));

        realEstateComponentService.updateRealEstateComponent(TEST_BUILDING);

        verifyPutRequest(REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_BUILDING));
    }

    @Test
    void testCreateLand() {
        stubPostResponse(REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_LAND));

        realEstateComponentService.createRealEstateComponent(TEST_LAND);

        verifyPostRequest(REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_LAND));
    }

    @Test
    void testUpdateLand() {
        stubPutResponse(REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_LAND));

        realEstateComponentService.updateRealEstateComponent(TEST_LAND);

        verifyPutRequest(REAL_ESTATE_COMPONENT_JSON, objectToJson(TEST_LAND));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID);

        realEstateComponentService.deleteRealEstateComponent(UUID.fromString(TEST_BUILDING_ID));

        verifyDeleteRequest(REAL_ESTATE_COMPONENT_JSON + "/" + TEST_BUILDING_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(REAL_ESTATE_COMPONENTS_JSON, objectToJson(successBatchResponse()));

        realEstateComponentService.createRealEstateComponents(Arrays.asList(TEST_BUILDING, TEST_BUILDING));

        verifyPostRequest(REAL_ESTATE_COMPONENTS_JSON, objectToJson(Arrays.asList(TEST_BUILDING, TEST_BUILDING)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(REAL_ESTATE_COMPONENTS_JSON, objectToJson(successBatchResponse()));

        realEstateComponentService.updateRealEstateComponents(Arrays.asList(TEST_BUILDING, TEST_BUILDING, TEST_LAND));

        verifyPutRequest(REAL_ESTATE_COMPONENTS_JSON, objectToJson(Arrays.asList(TEST_BUILDING, TEST_BUILDING, TEST_LAND)));
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(REAL_ESTATE_COMPONENTS_JSON, DataLoader.loadBatchFailedBuildings());

        realEstateComponentService.createRealEstateComponents(Arrays.asList(TEST_BUILDING, TEST_BUILDING));

        verifyPostRequest(REAL_ESTATE_COMPONENTS_JSON, objectToJson(Arrays.asList(TEST_BUILDING, TEST_BUILDING)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(REAL_ESTATE_COMPONENTS_JSON, DataLoader.loadBatchFailedBuildings());

        realEstateComponentService.updateRealEstateComponents(Arrays.asList(TEST_BUILDING, TEST_BUILDING));

        verifyPutRequest(REAL_ESTATE_COMPONENTS_JSON, objectToJson(Arrays.asList(TEST_BUILDING, TEST_BUILDING)));
    }

    @Test
    void testGetHistory() {
        stubGetResponse(getHistoryEndpoint(REAL_ESTATE_COMPONENT_JSON, UUID.fromString(TEST_BUILDING_ID)),
            new HashMap<>(), objectToJson(buildTwinHistory(TEST_BUILDING)));

        realEstateComponentService.getHistory(UUID.fromString(TEST_BUILDING_ID), null, null);

        verifyGetRequest(getHistoryEndpoint(REAL_ESTATE_COMPONENT_JSON, UUID.fromString(TEST_BUILDING_ID)), new HashMap<>());
    }

}
