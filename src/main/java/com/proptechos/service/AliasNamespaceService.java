package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ALIAS_NAMESPACE_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.AliasNamespace;
import java.util.UUID;

public class AliasNamespaceService extends PagedService<AliasNamespace> {

  AliasNamespaceService(String baseAppUrl) {
    super(baseAppUrl, ALIAS_NAMESPACE_JSON);
  }

  public AliasNamespace getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(AliasNamespace.class, ALIAS_NAMESPACE_JSON, id);
  }

  public AliasNamespace createAliasNamespace(AliasNamespace aliasNamespace) throws ProptechOsServiceException {
    return httpClient.createObject(AliasNamespace.class, ALIAS_NAMESPACE_JSON, aliasNamespace);
  }

  public AliasNamespace updateAliasNamespace(AliasNamespace aliasNamespace) throws ProptechOsServiceException {
    return httpClient.updateObject(AliasNamespace.class, ALIAS_NAMESPACE_JSON, aliasNamespace);
  }

  public void deleteAliasNamespace(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(ALIAS_NAMESPACE_JSON, id);
  }
}
