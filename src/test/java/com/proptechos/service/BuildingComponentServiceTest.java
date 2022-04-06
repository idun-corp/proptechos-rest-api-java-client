package com.proptechos.service;

import com.proptechos.model.common.IBuildingComponent;
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

import static com.proptechos.http.constants.ApiEndpoints.BUILDING_COMPONENTS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.BUILDING_COMPONENT_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class BuildingComponentServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedBuildingComponents();
    private static final String TEST_BC_ID = "397abf21-0e94-4bee-9bc9-0464a7ddd44a";
    private static final IBuildingComponent TEST_BC = buildBuildingComponent();

    private static BuildingComponentService buildingComponentService;

    @BeforeAll
    static void setUp() {
        buildingComponentService = serviceFactory.buildingComponentService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "50");

        stubGetResponse(BUILDING_COMPONENT_JSON, parameters, PAGED_DATA);

        buildingComponentService.getFirstPage(50);

        verifyGetRequest(BUILDING_COMPONENT_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "50");
        parameters2.put("size", "50");

        stubGetResponse(BUILDING_COMPONENT_JSON, parameters1, PAGED_DATA);
        stubGetResponse(BUILDING_COMPONENT_JSON, parameters2, PAGED_DATA);

        buildingComponentService.getLastPage(50);

        verifyGetRequest(BUILDING_COMPONENT_JSON, parameters1);
        verifyGetRequest(BUILDING_COMPONENT_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "50");

        stubGetResponse(BUILDING_COMPONENT_JSON, parameters, PAGED_DATA);

        buildingComponentService.getPage(1, 50);

        verifyGetRequest(BUILDING_COMPONENT_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "50");

        stubGetResponse(BUILDING_COMPONENT_JSON, parameters1, PAGED_DATA);
        stubGetResponse(BUILDING_COMPONENT_JSON, parameters2, PAGED_DATA);

        Paged<IBuildingComponent> paged = buildingComponentService.getFirstPage(50);
        buildingComponentService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(BUILDING_COMPONENT_JSON, parameters1);
        verifyGetRequest(BUILDING_COMPONENT_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID,
            new HashMap<>(), DataLoader.loadSingleBuildingComponent());

        buildingComponentService.getById(UUID.fromString(TEST_BC_ID));

        verifyGetRequest(BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(BUILDING_COMPONENT_JSON, objectToJson(TEST_BC));

        buildingComponentService.createBuildingComponent(TEST_BC);

        verifyPostRequest(BUILDING_COMPONENT_JSON, objectToJson(TEST_BC));
    }

    @Test
    void testUpdate() {
        stubPutResponse(BUILDING_COMPONENT_JSON, objectToJson(TEST_BC));

        buildingComponentService.updateBuildingComponent(TEST_BC);

        verifyPutRequest(BUILDING_COMPONENT_JSON, objectToJson(TEST_BC));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID);

        buildingComponentService.deleteBuildingComponent(UUID.fromString(TEST_BC_ID));

        verifyDeleteRequest(BUILDING_COMPONENT_JSON + "/" + TEST_BC_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(BUILDING_COMPONENTS_JSON, objectToJson(successBatchResponse()));

        buildingComponentService.createBuildingComponents(Arrays.asList(TEST_BC, TEST_BC));

        verifyPostRequest(BUILDING_COMPONENTS_JSON, objectToJson(Arrays.asList(TEST_BC, TEST_BC)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(BUILDING_COMPONENTS_JSON, objectToJson(successBatchResponse()));

        buildingComponentService.updateBuildingComponents(Arrays.asList(TEST_BC, TEST_BC));

        verifyPutRequest(BUILDING_COMPONENTS_JSON, objectToJson(Arrays.asList(TEST_BC, TEST_BC)));
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(BUILDING_COMPONENTS_JSON, DataLoader.loadBatchFailedBuildingComponents());

        buildingComponentService.createBuildingComponents(Arrays.asList(TEST_BC, TEST_BC));

        verifyPostRequest(BUILDING_COMPONENTS_JSON, objectToJson(Arrays.asList(TEST_BC, TEST_BC)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(BUILDING_COMPONENTS_JSON, DataLoader.loadBatchFailedBuildingComponents());

        buildingComponentService.updateBuildingComponents(Arrays.asList(TEST_BC, TEST_BC));

        verifyPutRequest(BUILDING_COMPONENTS_JSON, objectToJson(Arrays.asList(TEST_BC, TEST_BC)));
    }

    @Test
    void testGetHistory() {
        stubGetResponse(getHistoryEndpoint(BUILDING_COMPONENT_JSON, UUID.fromString(TEST_BC_ID)),
            new HashMap<>(), objectToJson(buildTwinHistory(TEST_BC)));

        buildingComponentService.getHistory(UUID.fromString(TEST_BC_ID), null, null);

        verifyGetRequest(getHistoryEndpoint(BUILDING_COMPONENT_JSON, UUID.fromString(TEST_BC_ID)), new HashMap<>());
    }

}
