package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.AliasNamespace;
import com.proptechos.model.BatchResponse;

import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.*;

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

    public BatchResponse<AliasNamespace> createAliasNamespaces(List<AliasNamespace> aliasNamespaces) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, ALIAS_NAMESPACES_JSON, aliasNamespaces);
    }

    public BatchResponse<AliasNamespace> updateAliasNamespaces(List<AliasNamespace> aliasNamespaces) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, ALIAS_NAMESPACES_JSON, aliasNamespaces);
    }
}
