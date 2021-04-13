package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.PROPERTY_OWNER_JSON;
import static com.proptechos.http.constants.ApiEndpoints.DEFAULT_PROPERTY_OWNER_JSON;
import static com.proptechos.utils.ValidationUtils.verifyGetRequest;
import static org.mockserver.model.HttpRequest.request;

import com.proptechos.model.PropertyOwner;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.DataLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.Parameter;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {9090})
public class PropertyOwnerServiceTest extends BaseServiceTest {

  private static final String PAGED_DATA = DataLoader.loadPagedPropertyOwners();
  private static final String TEST_PO_ID = "e7b277ee-40ab-4f10-8f42-7c20273c6f94";

  private static PropertyOwnerService propertyOwnerService;
  private final MockServerClient client;

  PropertyOwnerServiceTest(MockServerClient client) {
    this.client = client;
  }

  @BeforeAll
  static void setUp() {
    propertyOwnerService = serviceFactory.propertyOwnerService();
  }

  @AfterEach
  void clearMockServer() {
    client.clear(
        request().withPath(APP_CONTEXT + PROPERTY_OWNER_JSON));
    client.clear(
        request().withPath(APP_CONTEXT + PROPERTY_OWNER_JSON + "/" + TEST_PO_ID));
  }

  @Test
  void testGetFirstPage() {
    client.when(getRequest(PROPERTY_OWNER_JSON)).respond(okResponse(PAGED_DATA));

    propertyOwnerService.getFirstPage(10);

    verifyGetRequest(client, PROPERTY_OWNER_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "10"));
  }

  @Test
  void testGetLastPage() {
    client.when(getRequest(PROPERTY_OWNER_JSON)).respond(okResponse(PAGED_DATA));

    propertyOwnerService.getLastPage(10);

    verifyGetRequest(client, PROPERTY_OWNER_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "10"));

    verifyGetRequest(client, PROPERTY_OWNER_JSON,
        Parameter.param("page", "3"),
        Parameter.param("size", "10"));
  }

  @Test
  void testGetByPage() {
    client.when(getRequest(PROPERTY_OWNER_JSON)).respond(okResponse(PAGED_DATA));

    propertyOwnerService.getPage(1, 10);

    verifyGetRequest(client, PROPERTY_OWNER_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "10"));
  }

  @Test
  void testGetNextPage() {
    client.when(getRequest(PROPERTY_OWNER_JSON)).respond(okResponse(PAGED_DATA));

    Paged<PropertyOwner> paged = propertyOwnerService.getFirstPage(10);
    propertyOwnerService.getNextPage(paged.getPageMetadata());

    verifyGetRequest(client, PROPERTY_OWNER_JSON,
        Parameter.param("page", "0"),
        Parameter.param("size", "10"));

    verifyGetRequest(client, PROPERTY_OWNER_JSON,
        Parameter.param("page", "1"),
        Parameter.param("size", "10"));
  }

  @Test
  void testGetDefaultPropertyOwner() {
    client.when(getRequest(DEFAULT_PROPERTY_OWNER_JSON))
            .respond(okResponse(DataLoader.loadSinglePropertyOwner()));
    propertyOwnerService.getDefaultPropertyOwner();
    verifyGetRequest(client, DEFAULT_PROPERTY_OWNER_JSON);
  }

}
