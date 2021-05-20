package com.proptechos.service;

import com.proptechos.model.AliasNamespace;
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

import static com.proptechos.http.constants.ApiEndpoints.ALIAS_NAMESPACES_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ALIAS_NAMESPACE_JSON;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class AliasNamespaceServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedAliasNamespaces();
    private static final String TEST_AN_ID = "2afba45c-b50d-4e9b-b230-475b5464eceb";
    private static final AliasNamespace TEST_AN = buildAliasNamespace();

    private static AliasNamespaceService aliasNamespaceService;

    @BeforeAll
    static void setUp() {
        aliasNamespaceService = serviceFactory.aliasNamespaceService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "5");

        stubGetResponse(ALIAS_NAMESPACE_JSON, parameters, PAGED_DATA);

        aliasNamespaceService.getFirstPage(5);

        verifyGetRequest(ALIAS_NAMESPACE_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "5");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "3");
        parameters2.put("size", "5");

        stubGetResponse(ALIAS_NAMESPACE_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ALIAS_NAMESPACE_JSON, parameters2, PAGED_DATA);

        aliasNamespaceService.getLastPage(5);

        verifyGetRequest(ALIAS_NAMESPACE_JSON, parameters1);
        verifyGetRequest(ALIAS_NAMESPACE_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "5");

        stubGetResponse(ALIAS_NAMESPACE_JSON, parameters, PAGED_DATA);

        aliasNamespaceService.getPage(1, 5);

        verifyGetRequest(ALIAS_NAMESPACE_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "5");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "5");

        stubGetResponse(ALIAS_NAMESPACE_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ALIAS_NAMESPACE_JSON, parameters2, PAGED_DATA);

        Paged<AliasNamespace> paged = aliasNamespaceService.getFirstPage(5);
        aliasNamespaceService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(ALIAS_NAMESPACE_JSON, parameters1);
        verifyGetRequest(ALIAS_NAMESPACE_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID,
            new HashMap<>(), DataLoader.loadSingleAliasNamespace());

        aliasNamespaceService.getById(UUID.fromString(TEST_AN_ID));

        verifyGetRequest(ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(ALIAS_NAMESPACE_JSON, objectToJson(TEST_AN));

        aliasNamespaceService.createAliasNamespace(TEST_AN);

        verifyPostRequest(ALIAS_NAMESPACE_JSON, objectToJson(TEST_AN));
    }

    @Test
    void testUpdate() {
        stubPutResponse(ALIAS_NAMESPACE_JSON, objectToJson(TEST_AN));

        aliasNamespaceService.updateAliasNamespace(TEST_AN);

        verifyPutRequest(ALIAS_NAMESPACE_JSON, objectToJson(TEST_AN));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID);

        aliasNamespaceService.deleteAliasNamespace(UUID.fromString(TEST_AN_ID));

        verifyDeleteRequest(ALIAS_NAMESPACE_JSON + "/" + TEST_AN_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(ALIAS_NAMESPACES_JSON, objectToJson(successBatchResponse()));

        aliasNamespaceService.createAliasNamespaces(Arrays.asList(TEST_AN, TEST_AN));

        verifyPostRequest(ALIAS_NAMESPACES_JSON, objectToJson(Arrays.asList(TEST_AN, TEST_AN)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(ALIAS_NAMESPACES_JSON, objectToJson(successBatchResponse()));

        aliasNamespaceService.updateAliasNamespaces(Arrays.asList(TEST_AN, TEST_AN));

        verifyPutRequest(ALIAS_NAMESPACES_JSON, objectToJson(Arrays.asList(TEST_AN, TEST_AN)));
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(ALIAS_NAMESPACES_JSON, DataLoader.loadBatchFailedAliasNamespaces());

        aliasNamespaceService.createAliasNamespaces(Arrays.asList(TEST_AN, TEST_AN));

        verifyPostRequest(ALIAS_NAMESPACES_JSON, objectToJson(Arrays.asList(TEST_AN, TEST_AN)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(ALIAS_NAMESPACES_JSON, DataLoader.loadBatchFailedAliasNamespaces());

        aliasNamespaceService.updateAliasNamespaces(Arrays.asList(TEST_AN, TEST_AN));

        verifyPutRequest(ALIAS_NAMESPACES_JSON, objectToJson(Arrays.asList(TEST_AN, TEST_AN)));
    }
}
