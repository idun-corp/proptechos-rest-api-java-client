package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.STOREY_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.buildingcomponent.Storey;
import java.util.UUID;

public class StoreyService extends PagedService<Storey> {
  
  StoreyService(String baseAppUrl) {
    super(baseAppUrl, STOREY_JSON, Storey.class);
  }

  public Storey getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(typeClazz, STOREY_JSON, id);
  }

  public Storey createStorey(Storey storey) throws ProptechOsServiceException {
    return httpClient.createObject(typeClazz, STOREY_JSON, storey);
  }

  public Storey updateStorey(Storey storey) throws ProptechOsServiceException {
    return httpClient.updateObject(typeClazz, STOREY_JSON, storey);
  }

  public void deleteStorey(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(STOREY_JSON, id);
  }

}
