package com.proptechos.service;

import com.proptechos.model.Actuator;
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

import static com.proptechos.http.constants.ApiEndpoints.ACTUATORS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ACTUATOR_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class ActuatorServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedActuators();
    private static final String TEST_ACTUATOR_ID = "846f0b95-5c26-4cda-a236-0b35f1d362e3";
    private static final String TEST_ACTUATION_ID = "0a9146af-e660-4983-b62a-e6a1596e6e8d";
    private static final Actuator TEST_ACTUATOR = buildActuator();

    private static ActuatorService actuatorService;

    @BeforeAll
    static void setUp() {
        actuatorService = serviceFactory.actuatorService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "15");

        stubGetResponse(ACTUATOR_JSON, parameters, PAGED_DATA);

        actuatorService.getFirstPage(15);

        verifyGetRequest(ACTUATOR_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "10");
        parameters2.put("size", "15");

        stubGetResponse(ACTUATOR_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ACTUATOR_JSON, parameters2, PAGED_DATA);

        actuatorService.getLastPage(15);

        verifyGetRequest(ACTUATOR_JSON, parameters1);
        verifyGetRequest(ACTUATOR_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "15");

        actuatorService.getPage(1, 15);

        verifyGetRequest(ACTUATOR_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "15");

        stubGetResponse(ACTUATOR_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ACTUATOR_JSON, parameters2, PAGED_DATA);

        Paged<Actuator> paged = actuatorService.getFirstPage(15);
        actuatorService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(ACTUATOR_JSON, parameters1);
        verifyGetRequest(ACTUATOR_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID,
            new HashMap<>(), DataLoader.loadSingleActuator());

        actuatorService.getById(UUID.fromString(TEST_ACTUATOR_ID));

        verifyGetRequest(ACTUATOR_JSON, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(ACTUATOR_JSON, objectToJson(TEST_ACTUATOR));

        actuatorService.createActuator(TEST_ACTUATOR);

        verifyPostRequest(ACTUATOR_JSON, objectToJson(TEST_ACTUATOR));
    }

    @Test
    void testUpdate() {
        stubPutResponse(ACTUATOR_JSON, objectToJson(TEST_ACTUATOR));

        actuatorService.updateActuator(TEST_ACTUATOR);

        verifyPutRequest(ACTUATOR_JSON, objectToJson(TEST_ACTUATOR));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID);

        actuatorService.deleteActuator(UUID.fromString(TEST_ACTUATOR_ID));

        verifyDeleteRequest(ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(ACTUATORS_JSON, objectToJson(successBatchResponse()));

        actuatorService.createActuators(Arrays.asList(TEST_ACTUATOR, TEST_ACTUATOR));

        verifyPostRequest(ACTUATORS_JSON, objectToJson(Arrays.asList(TEST_ACTUATOR, TEST_ACTUATOR)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(ACTUATORS_JSON, objectToJson(successBatchResponse()));

        actuatorService.updateActuators(Arrays.asList(TEST_ACTUATOR, TEST_ACTUATOR));

        verifyPutRequest(ACTUATORS_JSON, objectToJson(Arrays.asList(TEST_ACTUATOR, TEST_ACTUATOR)));
    }

    @Test
    void testGetActuationResponse() {
        stubGetResponse(ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID + "/actuation/" + TEST_ACTUATION_ID,
            new HashMap<>(), DataLoader.loadActuationResponse());

        actuatorService.getActuationResult(UUID.fromString(TEST_ACTUATOR_ID), UUID.fromString(TEST_ACTUATION_ID));

        verifyGetRequest(ACTUATOR_JSON + "/" + TEST_ACTUATOR_ID + "/actuation/" + TEST_ACTUATION_ID, new HashMap<>());
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(ACTUATORS_JSON, DataLoader.loadBatchFailedActuators());

        actuatorService.createActuators(Arrays.asList(TEST_ACTUATOR, TEST_ACTUATOR));

        verifyPostRequest(ACTUATORS_JSON, objectToJson(Arrays.asList(TEST_ACTUATOR, TEST_ACTUATOR)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(ACTUATORS_JSON, DataLoader.loadBatchFailedActuators());

        actuatorService.updateActuators(Arrays.asList(TEST_ACTUATOR, TEST_ACTUATOR));

        verifyPutRequest(ACTUATORS_JSON, objectToJson(Arrays.asList(TEST_ACTUATOR, TEST_ACTUATOR)));
    }

    @Test
    void testGetHistory() {
        stubGetResponse(getHistoryEndpoint(ACTUATOR_JSON, UUID.fromString(TEST_ACTUATOR_ID)),
            new HashMap<>(), objectToJson(buildTwinHistory(TEST_ACTUATOR)));

        actuatorService.getHistory(UUID.fromString(TEST_ACTUATOR_ID), null, null);

        verifyGetRequest(getHistoryEndpoint(ACTUATOR_JSON, UUID.fromString(TEST_ACTUATOR_ID)), new HashMap<>());
    }

}
