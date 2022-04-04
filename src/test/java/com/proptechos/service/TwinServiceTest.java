package com.proptechos.service;

import com.proptechos.utils.DataLoader;
import com.proptechos.utils.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.TWIN_JSON;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;

@ExtendWith(WireMockExtension.class)
public class TwinServiceTest extends BaseServiceTest {
    private static final String PAGED_DATA = DataLoader.loadPagedBuildingComponents();
    private static final String TEST_BC_ID = "397abf21-0e94-4bee-9bc9-0464a7ddd44a";

    private static TwinService twinService;

    @BeforeAll
    static void setUp() {
        twinService = serviceFactory.twinService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "50");

        stubGetResponse(TWIN_JSON, parameters, PAGED_DATA);

        twinService.getFirstPage(50);

        verifyGetRequest(TWIN_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "50");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "50");
        parameters2.put("size", "50");

        stubGetResponse(TWIN_JSON, parameters1, PAGED_DATA);
        stubGetResponse(TWIN_JSON, parameters2, PAGED_DATA);

        twinService.getLastPage(50);

        verifyGetRequest(TWIN_JSON, parameters1);
        verifyGetRequest(TWIN_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(TWIN_JSON + "/" + TEST_BC_ID,
                new HashMap<>(), DataLoader.loadSingleBuildingComponent());

        twinService.getById(UUID.fromString(TEST_BC_ID));

        verifyGetRequest(TWIN_JSON + "/" + TEST_BC_ID, new HashMap<>());
    }

}
