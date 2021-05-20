package com.proptechos.service;

import com.proptechos.model.Point;
import com.proptechos.model.RealEstate;
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

import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATES_JSON;
import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_JSON;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class RealEstateServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedRealEstates();
    private static final String LIST_DATA = DataLoader.loadRealEstateList();
    private static final String TEST_RE_ID = "1b5d06c9-9f43-46b0-93f4-e9e5d9e3c972";
    private static final RealEstate TEST_RE = buildRealEstate();

    private static RealEstateService realEstateService;

    @BeforeAll
    static void setUp() {
        realEstateService = serviceFactory.realEstateService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "15");

        stubGetResponse(REAL_ESTATE_JSON, parameters, PAGED_DATA);

        realEstateService.getFirstPage(15);

        verifyGetRequest(REAL_ESTATE_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "2");
        parameters2.put("size", "15");

        stubGetResponse(REAL_ESTATE_JSON, parameters1, PAGED_DATA);
        stubGetResponse(REAL_ESTATE_JSON, parameters2, PAGED_DATA);

        realEstateService.getLastPage(15);

        verifyGetRequest(REAL_ESTATE_JSON, parameters1);
        verifyGetRequest(REAL_ESTATE_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "15");

        stubGetResponse(REAL_ESTATE_JSON, parameters, PAGED_DATA);

        realEstateService.getPage(1, 15);

        verifyGetRequest(REAL_ESTATE_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "15");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "15");

        stubGetResponse(REAL_ESTATE_JSON, parameters1, PAGED_DATA);
        stubGetResponse(REAL_ESTATE_JSON, parameters2, PAGED_DATA);

        Paged<RealEstate> paged = realEstateService.getFirstPage(15);
        realEstateService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(REAL_ESTATE_JSON, parameters1);
        verifyGetRequest(REAL_ESTATE_JSON, parameters2);
    }

    @Test
    void testGetInRange() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("lat", "59.0");
        parameters.put("lon", "18.0");
        parameters.put("dist", "100.0");

        stubGetResponse(REAL_ESTATE_JSON  + "/inrange", parameters, LIST_DATA);

        realEstateService.getRealEstatesInRange(new Point(59, 18, 100));

        verifyGetRequest(REAL_ESTATE_JSON + "/inrange", parameters);
    }

    @Test
    void testGetById() {
        stubGetResponse(REAL_ESTATE_JSON + "/" + TEST_RE_ID,
            new HashMap<>(), DataLoader.loadSingleRealEstate());

        realEstateService.getById(UUID.fromString(TEST_RE_ID));

        verifyGetRequest(REAL_ESTATE_JSON + "/" + TEST_RE_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(REAL_ESTATE_JSON, objectToJson(TEST_RE));

        realEstateService.createRealEstate(TEST_RE);

        verifyPostRequest(REAL_ESTATE_JSON, objectToJson(TEST_RE));
    }

    @Test
    void testUpdate() {
        stubPutResponse(REAL_ESTATE_JSON, objectToJson(TEST_RE));

        realEstateService.updateRealEstate(TEST_RE);

        verifyPutRequest(REAL_ESTATE_JSON, objectToJson(TEST_RE));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(REAL_ESTATE_JSON + "/" + TEST_RE_ID);;

        realEstateService.deleteRealEstate(UUID.fromString(TEST_RE_ID));

        verifyDeleteRequest(REAL_ESTATE_JSON + "/" + TEST_RE_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(REAL_ESTATES_JSON, objectToJson(successBatchResponse()));

        realEstateService.createRealEstates(Arrays.asList(TEST_RE, TEST_RE));

        verifyPostRequest(REAL_ESTATES_JSON, objectToJson(Arrays.asList(TEST_RE, TEST_RE)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(REAL_ESTATES_JSON, objectToJson(successBatchResponse()));

        realEstateService.updateRealEstates(Arrays.asList(TEST_RE, TEST_RE));

        verifyPutRequest(REAL_ESTATES_JSON, objectToJson(Arrays.asList(TEST_RE, TEST_RE)));
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(REAL_ESTATES_JSON, DataLoader.loadBatchFailedRealEstates());

        realEstateService.createRealEstates(Arrays.asList(TEST_RE, TEST_RE));

        verifyPostRequest(REAL_ESTATES_JSON, objectToJson(Arrays.asList(TEST_RE, TEST_RE)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(REAL_ESTATES_JSON, DataLoader.loadBatchFailedRealEstates());

        realEstateService.updateRealEstates(Arrays.asList(TEST_RE, TEST_RE));

        verifyPutRequest(REAL_ESTATES_JSON, objectToJson(Arrays.asList(TEST_RE, TEST_RE)));
    }
}
