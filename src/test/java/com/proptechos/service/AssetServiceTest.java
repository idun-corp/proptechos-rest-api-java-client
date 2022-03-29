package com.proptechos.service;

import com.proptechos.model.Asset;
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

import static com.proptechos.http.constants.ApiEndpoints.ASSETS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ASSET_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class AssetServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedAssets();
    private static final String TEST_ASSET_ID = "3f065d63-7d95-417d-9b24-19647294aa9c";
    private static final Asset TEST_ASSET = buildAsset();

    private static AssetService assetService;

    @BeforeAll
    static void setUp() {
        assetService = serviceFactory.assetService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "15");
        
        stubGetResponse(ASSET_JSON, parameters, PAGED_DATA);

        assetService.getFirstPage(15);

        verifyGetRequest(ASSET_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "4");
        parameters2.put("size", "15");

        stubGetResponse(ASSET_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ASSET_JSON, parameters2, PAGED_DATA);

        assetService.getLastPage(15);

        verifyGetRequest(ASSET_JSON, parameters1);
        verifyGetRequest(ASSET_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "15");

        stubGetResponse(ASSET_JSON, parameters, PAGED_DATA);

        assetService.getPage(1, 15);

        verifyGetRequest(ASSET_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "15");

        stubGetResponse(ASSET_JSON, parameters1, PAGED_DATA);
        stubGetResponse(ASSET_JSON, parameters2, PAGED_DATA);

        Paged<Asset> paged = assetService.getFirstPage(15);
        assetService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(ASSET_JSON, parameters1);
        verifyGetRequest(ASSET_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(ASSET_JSON + "/" + TEST_ASSET_ID,
            new HashMap<>(), DataLoader.loadSingleAsset());

        assetService.getById(UUID.fromString(TEST_ASSET_ID));

        verifyGetRequest(ASSET_JSON + "/" + TEST_ASSET_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(ASSET_JSON, objectToJson(TEST_ASSET));

        assetService.createAsset(TEST_ASSET);

        verifyPostRequest(ASSET_JSON, objectToJson(TEST_ASSET));
    }

    @Test
    void testUpdate() {
        stubPutResponse(ASSET_JSON, objectToJson(TEST_ASSET));

        assetService.updateAsset(TEST_ASSET);

        verifyPutRequest(ASSET_JSON, objectToJson(TEST_ASSET));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(ASSET_JSON + "/" + TEST_ASSET_ID);

        assetService.deleteAsset(UUID.fromString(TEST_ASSET_ID));

        verifyDeleteRequest(ASSET_JSON + "/" + TEST_ASSET_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(ASSETS_JSON, objectToJson(successBatchResponse()));

        assetService.createAssets(Arrays.asList(TEST_ASSET, TEST_ASSET));

        verifyPostRequest(ASSETS_JSON, objectToJson(Arrays.asList(TEST_ASSET, TEST_ASSET)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(ASSETS_JSON, objectToJson(successBatchResponse()));

        assetService.updateAssets(Arrays.asList(TEST_ASSET, TEST_ASSET));

        verifyPutRequest(ASSETS_JSON, objectToJson(Arrays.asList(TEST_ASSET, TEST_ASSET)));
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(ASSETS_JSON, DataLoader.loadBatchFailedAssets());

        assetService.createAssets(Arrays.asList(TEST_ASSET, TEST_ASSET));

        verifyPostRequest(ASSETS_JSON, objectToJson(Arrays.asList(TEST_ASSET, TEST_ASSET)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(ASSETS_JSON, DataLoader.loadBatchFailedAssets());

        assetService.updateAssets(Arrays.asList(TEST_ASSET, TEST_ASSET));

        verifyPutRequest(ASSETS_JSON, objectToJson(Arrays.asList(TEST_ASSET, TEST_ASSET)));
    }

    @Test
    void testGetHistory() {
        stubGetResponse(getHistoryEndpoint(ASSET_JSON, UUID.fromString(TEST_ASSET_ID)),
            new HashMap<>(), objectToJson(buildTwinHistory(TEST_ASSET)));

        assetService.getHistory(UUID.fromString(TEST_ASSET_ID), null, null);

        verifyGetRequest(getHistoryEndpoint(ASSET_JSON, UUID.fromString(TEST_ASSET_ID)), new HashMap<>());
    }
}
