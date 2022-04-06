package com.proptechos.service;

import com.proptechos.model.actuation.ActuationInterface;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import com.proptechos.utils.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.ACTUATION_INTERFACE_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class ActuationInterfaceServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedActuationInterfaces();
    private static final String TEST_AI_ID = "c61c5a10-83ac-40b8-8f53-2e4b2f6df801";
    private static final ActuationInterface TEST_ACTUATION_INTERFACE = buildActuationInterface();

    private static ActuationInterfaceService actuationInterfaceService;

    @BeforeAll
    static void setUp() {
        actuationInterfaceService = serviceFactory.actuationInterfaceService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "5");

        stubGetResponse(ACTUATION_INTERFACE_JSON, parameters, PAGED_DATA);

        actuationInterfaceService.getFirstPage(5);

        verifyGetRequest(ACTUATION_INTERFACE_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "5");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "2");
        parameters2.put("size", "5");

        stubGetResponse(ACTUATION_INTERFACE_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ACTUATION_INTERFACE_JSON, parameters2, PAGED_DATA);

        actuationInterfaceService.getLastPage(5);

        verifyGetRequest(ACTUATION_INTERFACE_JSON, parameters1);
        verifyGetRequest(ACTUATION_INTERFACE_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "5");

        stubGetResponse(ACTUATION_INTERFACE_JSON, parameters, PAGED_DATA);

        actuationInterfaceService.getPage(1, 5);

        verifyGetRequest(ACTUATION_INTERFACE_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "5");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "5");

        stubGetResponse(ACTUATION_INTERFACE_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ACTUATION_INTERFACE_JSON, parameters2, PAGED_DATA);

        Paged<ActuationInterface> paged = actuationInterfaceService.getFirstPage(5);
        actuationInterfaceService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(ACTUATION_INTERFACE_JSON, parameters1);
        verifyGetRequest(ACTUATION_INTERFACE_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID,
            new HashMap<>(), DataLoader.loadSingleActuationInterface());

        actuationInterfaceService.getById(UUID.fromString(TEST_AI_ID));

        verifyGetRequest(ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(ACTUATION_INTERFACE_JSON, objectToJson(TEST_ACTUATION_INTERFACE));

        actuationInterfaceService
            .createActuationInterface(TEST_ACTUATION_INTERFACE);

        verifyPostRequest(ACTUATION_INTERFACE_JSON, objectToJson(TEST_ACTUATION_INTERFACE));
    }

    @Test
    void testUpdate() {
        stubPutResponse(ACTUATION_INTERFACE_JSON, objectToJson(TEST_ACTUATION_INTERFACE));

        actuationInterfaceService
            .updateActuationInterface(TEST_ACTUATION_INTERFACE);

        verifyPutRequest(ACTUATION_INTERFACE_JSON, objectToJson(TEST_ACTUATION_INTERFACE));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID);

        actuationInterfaceService
            .deleteActuationInterface(UUID.fromString(TEST_AI_ID));

        verifyDeleteRequest(ACTUATION_INTERFACE_JSON + "/" + TEST_AI_ID);
    }

    @Test
    void testGetHistory() {
        stubGetResponse(getHistoryEndpoint(ACTUATION_INTERFACE_JSON, UUID.fromString(TEST_AI_ID)),
            new HashMap<>(), objectToJson(buildTwinHistory(TEST_ACTUATION_INTERFACE)));

        actuationInterfaceService.getHistory(UUID.fromString(TEST_AI_ID), null, null);

        verifyGetRequest(getHistoryEndpoint(ACTUATION_INTERFACE_JSON, UUID.fromString(TEST_AI_ID)), new HashMap<>());
    }
}
