package com.proptechos.service;

import com.proptechos.model.common.IDevice;
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

import static com.proptechos.http.constants.ApiEndpoints.DEVICES_JSON;
import static com.proptechos.http.constants.ApiEndpoints.DEVICE_JSON;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class DeviceServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedDevices();
    private static final String TEST_DEVICE_ID = "5cd99e11-aa99-43b6-b5f5-01a0033aa940";
    private static final IDevice TEST_DEVICE = buildDevice();

    private static DeviceService deviceService;

    @BeforeAll
    static void setUp() {
        deviceService = serviceFactory.deviceService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "50");

        stubGetResponse(DEVICE_JSON, parameters, PAGED_DATA);

        deviceService.getFirstPage(50);

        verifyGetRequest(DEVICE_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "136");
        parameters2.put("size", "50");

        stubGetResponse(DEVICE_JSON, parameters1, PAGED_DATA);
        stubGetResponse(DEVICE_JSON, parameters2, PAGED_DATA);

        deviceService.getLastPage(50);

        verifyGetRequest(DEVICE_JSON, parameters1);
        verifyGetRequest(DEVICE_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "32");
        parameters.put("size", "50");

        stubGetResponse(DEVICE_JSON, parameters, PAGED_DATA);

        deviceService.getPage(32, 50);

        verifyGetRequest(DEVICE_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "50");

        stubGetResponse(DEVICE_JSON, parameters1, PAGED_DATA);
        stubGetResponse(DEVICE_JSON, parameters2, PAGED_DATA);

        Paged<IDevice> paged = deviceService.getFirstPage(50);
        deviceService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(DEVICE_JSON, parameters1);
        verifyGetRequest(DEVICE_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(DEVICE_JSON + "/" + TEST_DEVICE_ID,
            new HashMap<>(), DataLoader.loadSingleDevice());

        deviceService.getById(UUID.fromString(TEST_DEVICE_ID));

        verifyGetRequest(DEVICE_JSON + "/" + TEST_DEVICE_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(DEVICE_JSON, objectToJson(TEST_DEVICE));

        deviceService.createDevice(TEST_DEVICE);

        verifyPostRequest(DEVICE_JSON, objectToJson(TEST_DEVICE));
    }

    @Test
    void testUpdate() {
        stubPutResponse(DEVICE_JSON, objectToJson(TEST_DEVICE));

        deviceService.updateDevice(TEST_DEVICE);

        verifyPutRequest(DEVICE_JSON, objectToJson(TEST_DEVICE));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(DEVICE_JSON + "/" + TEST_DEVICE_ID);

        deviceService.deleteDevice(UUID.fromString(TEST_DEVICE_ID));

        verifyDeleteRequest(DEVICE_JSON + "/" + TEST_DEVICE_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(DEVICES_JSON, objectToJson(successBatchResponse()));

        deviceService.createDevices(Arrays.asList(TEST_DEVICE, TEST_DEVICE));

        verifyPostRequest(DEVICES_JSON, objectToJson(Arrays.asList(TEST_DEVICE, TEST_DEVICE)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(DEVICES_JSON, objectToJson(successBatchResponse()));

        deviceService.updateDevices(Arrays.asList(TEST_DEVICE, TEST_DEVICE));

        verifyPutRequest(DEVICES_JSON, objectToJson(Arrays.asList(TEST_DEVICE, TEST_DEVICE)));
    }

    @Test
    void testFailedCreateBatch() {
        stubFailedPostResponse(DEVICES_JSON, DataLoader.loadBatchFailedDevices());

        deviceService.createDevices(Arrays.asList(TEST_DEVICE, TEST_DEVICE));

        verifyPostRequest(DEVICES_JSON, objectToJson(Arrays.asList(TEST_DEVICE, TEST_DEVICE)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubFailedPutResponse(DEVICES_JSON, DataLoader.loadBatchFailedDevices());

        deviceService.updateDevices(Arrays.asList(TEST_DEVICE, TEST_DEVICE));

        verifyPutRequest(DEVICES_JSON, objectToJson(Arrays.asList(TEST_DEVICE, TEST_DEVICE)));
    }
}
