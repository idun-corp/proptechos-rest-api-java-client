package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.System;
import com.proptechos.model.common.IBaseClass;
import com.proptechos.model.common.Paged;
import com.proptechos.service.filters.common.PageNumberFilter;
import com.proptechos.service.filters.common.PageSizeFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.SYSTEM_JSON;

public class SystemService extends PagedService<System> {

    SystemService(String baseAppUrl) {
        super(baseAppUrl, SYSTEM_JSON, System.class);
    }

    public System getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(System.class, SYSTEM_JSON, id);
    }

    public System createSystem(System system) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, SYSTEM_JSON, system);
    }

    public System updateSystem(System system) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, SYSTEM_JSON, system);
    }

    public List<UUID> includeAxioms(UUID systemId, UUID... axiomIds) {
        List<UUID> providedIds =
                axiomIds == null ? new ArrayList<>() : Arrays.asList(axiomIds);
        return httpClient.putList(UUID.class,
                SYSTEM_JSON + "/" + systemId + "/include",
                providedIds);
    }

    public List<UUID> excludeAxioms(UUID systemId, UUID... axiomIds) {
        List<UUID> providedIds =
                axiomIds == null ? new ArrayList<>() : Arrays.asList(axiomIds);
        return httpClient.putList(UUID.class,
                SYSTEM_JSON + "/" + systemId + "/exclude",
                providedIds);
    }

    public Paged<IBaseClass> getIncludedAxioms(UUID systemId, long pageNumber, long pageSize) {
        validatePageMetadata(pageNumber, pageSize);
        return httpClient.getPaged(IBaseClass.class,
                SYSTEM_JSON + "/" + systemId + "/includes",
                new PageSizeFilter(pageSize), new PageNumberFilter(pageNumber));
    }

    public void deleteSystem(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(SYSTEM_JSON, id);
    }

}
