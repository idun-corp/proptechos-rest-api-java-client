package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.common.IBaseClass;

import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.TWIN_JSON;

public class TwinService  extends PagedService<IBaseClass> {
    TwinService(String baseAppUrl) {
        super(baseAppUrl, TWIN_JSON, IBaseClass.class);
    }

    public IBaseClass getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(typeClazz, TWIN_JSON, id);
    }


}
