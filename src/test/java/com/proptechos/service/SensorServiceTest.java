package com.proptechos.service;

import com.proptechos.model.Sensor;
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

import static com.proptechos.http.constants.ApiEndpoints.SENSORS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.SENSOR_JSON;
import static com.proptechos.utils.TestDataHelper.*;
import static com.proptechos.utils.ValidationUtils.*;

@ExtendWith(WireMockExtension.class)
public class SensorServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedSensors();
    private static final String TEST_SENSOR_ID = "14f883c7-eda5-40a1-8280-002ffb237400";
    private static final Sensor TEST_SENSOR = buildSensor();

    private static SensorService sensorService;

    @BeforeAll
    static void setUp() {
        sensorService = serviceFactory.sensorService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "50");

        stubGetResponse(SENSOR_JSON, parameters, PAGED_DATA);

        sensorService.getFirstPage(50);

        verifyGetRequest(SENSOR_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "101");
        parameters2.put("size", "50");

        stubGetResponse(SENSOR_JSON, parameters1, PAGED_DATA);
        stubGetResponse(SENSOR_JSON, parameters2, PAGED_DATA);

        sensorService.getLastPage(50);

        verifyGetRequest(SENSOR_JSON, parameters1);
        verifyGetRequest(SENSOR_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "50");

        stubGetResponse(SENSOR_JSON, parameters, PAGED_DATA);

        sensorService.getPage(1, 50);

        verifyGetRequest(SENSOR_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "50");

        stubGetResponse(SENSOR_JSON, parameters1, PAGED_DATA);
        stubGetResponse(SENSOR_JSON, parameters2, PAGED_DATA);

        Paged<Sensor> paged = sensorService.getFirstPage(50);
        sensorService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(SENSOR_JSON, parameters1);
        verifyGetRequest(SENSOR_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(SENSOR_JSON + "/" + TEST_SENSOR_ID,
            new HashMap<>(), DataLoader.loadSingleSensor());

        sensorService.getById(UUID.fromString(TEST_SENSOR_ID));

        verifyGetRequest(SENSOR_JSON + "/" + TEST_SENSOR_ID, new HashMap<>());
    }

    @Test
    void testCreate() {
        stubPostResponse(SENSOR_JSON, objectToJson(TEST_SENSOR));

        sensorService.createSensor(TEST_SENSOR);

        verifyPostRequest(SENSOR_JSON, objectToJson(TEST_SENSOR));
    }

    @Test
    void testUpdate() {
        stubPutResponse(SENSOR_JSON, objectToJson(TEST_SENSOR));

        sensorService.updateSensor(TEST_SENSOR);

        verifyPutRequest(SENSOR_JSON, objectToJson(TEST_SENSOR));
    }

    @Test
    void testDelete() {
        stubDeleteResponse(SENSOR_JSON + "/" + TEST_SENSOR_ID);

        sensorService.deleteSensor(UUID.fromString(TEST_SENSOR_ID));

        verifyDeleteRequest(SENSOR_JSON + "/" + TEST_SENSOR_ID);
    }

    @Test
    void testCreateBatch() {
        stubPostResponse(SENSORS_JSON, objectToJson(successBatchResponse()));

        sensorService.createSensors(Arrays.asList(TEST_SENSOR, TEST_SENSOR));

        verifyPostRequest(SENSORS_JSON, objectToJson(Arrays.asList(TEST_SENSOR, TEST_SENSOR)));
    }

    @Test
    void testUpdateBatch() {
        stubPutResponse(SENSORS_JSON, objectToJson(successBatchResponse()));

        sensorService.updateSensors(Arrays.asList(TEST_SENSOR, TEST_SENSOR));

        verifyPutRequest(SENSORS_JSON, objectToJson(Arrays.asList(TEST_SENSOR, TEST_SENSOR)));
    }

    @Test
    void testFailedCreateBatch() {
        stubPostResponse(SENSORS_JSON, DataLoader.loadBatchFailedSensors());

        sensorService.createSensors(Arrays.asList(TEST_SENSOR, TEST_SENSOR));

        verifyPostRequest(SENSORS_JSON, objectToJson(Arrays.asList(TEST_SENSOR, TEST_SENSOR)));
    }

    @Test
    void testFailedUpdateBatch() {
        stubPutResponse(SENSORS_JSON, DataLoader.loadBatchFailedSensors());

        sensorService.updateSensors(Arrays.asList(TEST_SENSOR, TEST_SENSOR));

        verifyPutRequest(SENSORS_JSON, objectToJson(Arrays.asList(TEST_SENSOR, TEST_SENSOR)));
    }
}
