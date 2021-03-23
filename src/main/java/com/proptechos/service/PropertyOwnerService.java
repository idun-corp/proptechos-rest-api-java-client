package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.PROPERTY_OWNER_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.PropertyOwner;
import java.util.UUID;

public class PropertyOwnerService extends PagedService<PropertyOwner> {

  PropertyOwnerService(String baseAppUrl) {
    super(baseAppUrl, PROPERTY_OWNER_JSON, PropertyOwner.class);
  }

  public PropertyOwner getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(PropertyOwner.class, PROPERTY_OWNER_JSON, id);
  }
}
