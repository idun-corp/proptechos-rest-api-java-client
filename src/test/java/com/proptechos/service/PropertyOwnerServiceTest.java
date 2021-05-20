package com.proptechos.service;

import com.proptechos.model.PropertyOwner;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import com.proptechos.utils.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.DEFAULT_PROPERTY_OWNER_JSON;
import static com.proptechos.http.constants.ApiEndpoints.PROPERTY_OWNER_JSON;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;

@ExtendWith(WireMockExtension.class)
public class PropertyOwnerServiceTest extends BaseServiceTest {

    private static final String PAGED_DATA = DataLoader.loadPagedPropertyOwners();
    private static final String TEST_PO_ID = "e7b277ee-40ab-4f10-8f42-7c20273c6f94";

    private static PropertyOwnerService propertyOwnerService;

    @BeforeAll
    static void setUp() {
        propertyOwnerService = serviceFactory.propertyOwnerService();
    }

    @Test
    void testGetFirstPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "0");
        parameters.put("size", "10");

        stubGetResponse(PROPERTY_OWNER_JSON, parameters, PAGED_DATA);
        
        propertyOwnerService.getFirstPage(10);

        verifyGetRequest(PROPERTY_OWNER_JSON, parameters);
    }

    @Test
    void testGetLastPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "10");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "3");
        parameters2.put("size", "10");

        stubGetResponse(PROPERTY_OWNER_JSON, parameters1, PAGED_DATA);
        stubGetResponse(PROPERTY_OWNER_JSON, parameters2, PAGED_DATA);

        propertyOwnerService.getLastPage(10);

        verifyGetRequest(PROPERTY_OWNER_JSON, parameters1);
        verifyGetRequest(PROPERTY_OWNER_JSON, parameters2);
    }

    @Test
    void testGetByPage() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", "1");
        parameters.put("size", "10");

        stubGetResponse(PROPERTY_OWNER_JSON, parameters, PAGED_DATA);

        propertyOwnerService.getPage(1, 10);

        verifyGetRequest(PROPERTY_OWNER_JSON, parameters);
    }

    @Test
    void testGetNextPage() {
        Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("page", "0");
        parameters1.put("size", "10");

        Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("page", "1");
        parameters2.put("size", "10");

        stubGetResponse(PROPERTY_OWNER_JSON, parameters1, PAGED_DATA);
        stubGetResponse(PROPERTY_OWNER_JSON, parameters2, PAGED_DATA);

        Paged<PropertyOwner> paged = propertyOwnerService.getFirstPage(10);
        propertyOwnerService.getNextPage(paged.getPageMetadata());

        verifyGetRequest(PROPERTY_OWNER_JSON, parameters1);
        verifyGetRequest(PROPERTY_OWNER_JSON, parameters2);
    }

    @Test
    void testGetById() {
        stubGetResponse(PROPERTY_OWNER_JSON + "/" + TEST_PO_ID,
            new HashMap<>(), DataLoader.loadSinglePropertyOwner());

        propertyOwnerService.getById(UUID.fromString(TEST_PO_ID));

        verifyGetRequest(PROPERTY_OWNER_JSON + "/" + TEST_PO_ID, new HashMap<>());
    }

    @Test
    void testGetDefaultPropertyOwner() {
        stubGetResponse(DEFAULT_PROPERTY_OWNER_JSON,
            new HashMap<>(), DataLoader.loadSinglePropertyOwner());

        propertyOwnerService.getDefaultPropertyOwner();

        verifyGetRequest(DEFAULT_PROPERTY_OWNER_JSON, new HashMap<>());
    }

}
