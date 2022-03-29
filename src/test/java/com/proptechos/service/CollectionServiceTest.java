package com.proptechos.service;

import com.proptechos.model.Collection;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import com.proptechos.utils.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.COLLECTION_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WireMockExtension.class)
public class CollectionServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedCollection();
    private static final String TEST_COLLECTION_ID = "1b5d06c9-9f43-46b0-93f4-e9e5d9e3c972";
    private static final Collection TEST_COLLECTION = buildCollection();
    private static final UUID[] TEST_ID_LIST = new UUID[]{
        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()
    };

    private static CollectionService collectionService;

    @BeforeAll
    static void setUp() {
        collectionService = serviceFactory.collectionService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "15");

        stubGetResponse(COLLECTION_JSON, parameters, PAGED_DATA);

        collectionService.getFirstPage(15);

        verifyGetRequest(COLLECTION_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "2");
        parameters2.put("size", "15");

        stubGetResponse(COLLECTION_JSON, parameters1, PAGED_DATA);
        stubGetResponse(COLLECTION_JSON, parameters2, PAGED_DATA);

        collectionService.getLastPage(15);

        verifyGetRequest(COLLECTION_JSON, parameters1);
        verifyGetRequest(COLLECTION_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "15");

        stubGetResponse(COLLECTION_JSON, parameters, PAGED_DATA);

        collectionService.getPage(1, 15);

        verifyGetRequest(COLLECTION_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "15");

        stubGetResponse(COLLECTION_JSON, parameters1, PAGED_DATA);
        stubGetResponse(COLLECTION_JSON, parameters2, PAGED_DATA);

        Paged<Collection> paged = collectionService.getFirstPage(15);
        collectionService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(COLLECTION_JSON, parameters1);
        verifyGetRequest(COLLECTION_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(COLLECTION_JSON + "/" + TEST_COLLECTION_ID,
            new HashMap<>(), DataLoader.loadSingleCollection());

        collectionService.getById(UUID.fromString(TEST_COLLECTION_ID));

        verifyGetRequest(COLLECTION_JSON + "/" + TEST_COLLECTION_ID, new HashMap<>());
    }

    @Test
    void testGetIncludedAxioms() {
        String requestPath = COLLECTION_JSON + "/" + TEST_COLLECTION_ID + "/includes";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "10");

        stubGetResponse(requestPath, parameters, DataLoader.loadPagedDevices());

        collectionService.getIncludedAxioms(
            UUID.fromString(TEST_COLLECTION_ID), 0, 10);

        verifyGetRequest(requestPath, parameters);
    }

    @Test
    void testCreate() {
        stubPostResponse(COLLECTION_JSON, objectToJson(TEST_COLLECTION));

        collectionService.createCollection(TEST_COLLECTION);

        verifyPostRequest(COLLECTION_JSON, objectToJson(TEST_COLLECTION));
    }

    @Test
    void testUpdate() {
        stubPutResponse(COLLECTION_JSON, objectToJson(TEST_COLLECTION));

        collectionService.updateCollection(TEST_COLLECTION);

        verifyPutRequest(COLLECTION_JSON, objectToJson(TEST_COLLECTION));
    }

    @Test
    void testIncludeAxioms() {
        String requestPath = COLLECTION_JSON + "/" + TEST_COLLECTION_ID + "/include";
        stubPutResponse(requestPath, objectToJson(TEST_ID_LIST));

        List<UUID> included = collectionService.includeAxioms(UUID.fromString(TEST_COLLECTION_ID),
            TEST_ID_LIST[0], TEST_ID_LIST[1], TEST_ID_LIST[2], TEST_ID_LIST[3], TEST_ID_LIST[4]);

        assertEquals(5, included.size());

        verifyPutRequest(requestPath, objectToJson(TEST_ID_LIST));
    }

    @Test
    void testExcludeAxioms() {
        String requestPath = COLLECTION_JSON + "/" + TEST_COLLECTION_ID + "/exclude";
        stubPutResponse(requestPath, objectToJson(TEST_ID_LIST));

        List<UUID> included = collectionService.excludeAxioms(UUID.fromString(TEST_COLLECTION_ID),
            TEST_ID_LIST[0], TEST_ID_LIST[1], TEST_ID_LIST[2], TEST_ID_LIST[3], TEST_ID_LIST[4]);

        assertEquals(5, included.size());

        verifyPutRequest(requestPath, objectToJson(TEST_ID_LIST));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(COLLECTION_JSON + "/" + TEST_COLLECTION_ID);

        collectionService.deleteCollection(UUID.fromString(TEST_COLLECTION_ID));

        verifyDeleteRequest(COLLECTION_JSON + "/" + TEST_COLLECTION_ID);
    }

    @Test
    void testGetHistory() {
        stubGetResponse(getHistoryEndpoint(COLLECTION_JSON, UUID.fromString(TEST_COLLECTION_ID)),
            new HashMap<>(), objectToJson(buildTwinHistory(TEST_COLLECTION)));

        collectionService.getHistory(UUID.fromString(TEST_COLLECTION_ID), null, null);

        verifyGetRequest(getHistoryEndpoint(COLLECTION_JSON, UUID.fromString(TEST_COLLECTION_ID)), new HashMap<>());
    }
}
