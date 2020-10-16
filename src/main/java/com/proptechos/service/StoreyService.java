package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.STOREY_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Storey;
import java.util.UUID;

public class StoreyService extends PagedService<Storey> {
  
  StoreyService(String baseAppUrl) {
    super(baseAppUrl, STOREY_JSON);
  }

  public Storey getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(Storey.class, STOREY_JSON, id);
  }

  public Storey createStorey(Storey storey) throws ProptechOsServiceException {
    return httpClient.createObject(Storey.class, STOREY_JSON, storey);
  }

  public Storey updateStorey(Storey storey) throws ProptechOsServiceException {
    return httpClient.updateObject(Storey.class, STOREY_JSON, storey);
  }

  public void deleteStorey(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(STOREY_JSON, id);
  }

}
