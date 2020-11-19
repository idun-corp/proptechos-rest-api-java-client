package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ALIAS_NAMESPACE_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.AliasNamespace;
import java.util.UUID;

public class AliasNamespaceService extends PagedService<AliasNamespace> {

  AliasNamespaceService(String baseAppUrl) {
    super(baseAppUrl, ALIAS_NAMESPACE_JSON, AliasNamespace.class);
  }

  public AliasNamespace getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(typeClazz, ALIAS_NAMESPACE_JSON, id);
  }

  public AliasNamespace createAliasNamespace(AliasNamespace aliasNamespace) throws ProptechOsServiceException {
    return httpClient.createObject(typeClazz, ALIAS_NAMESPACE_JSON, aliasNamespace);
  }

  public AliasNamespace updateAliasNamespace(AliasNamespace aliasNamespace) throws ProptechOsServiceException {
    return httpClient.updateObject(typeClazz, ALIAS_NAMESPACE_JSON, aliasNamespace);
  }

  public void deleteAliasNamespace(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(ALIAS_NAMESPACE_JSON, id);
  }
}
