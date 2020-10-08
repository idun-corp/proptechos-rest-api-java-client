package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.RealEstate;
import java.util.UUID;

public class RealEstateService extends PagedService<RealEstate> {
  
  RealEstateService(String baseAppUrl) {
    super(baseAppUrl, REAL_ESTATE_JSON);
  }

  public RealEstate getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(RealEstate.class, REAL_ESTATE_JSON, id);
  }

  public RealEstate createRealEstate(RealEstate realEstate) throws ProptechOsServiceException {
    return httpClient.createObject(RealEstate.class, REAL_ESTATE_JSON, realEstate);
  }

  public RealEstate updateRealEstate(RealEstate realEstate) throws ProptechOsServiceException {
    return httpClient.updateObject(RealEstate.class, REAL_ESTATE_JSON, realEstate);
  }

  public void deleteRealEstate(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(REAL_ESTATE_JSON, id);
  }

}
