package com.proptechos.http;

import com.proptechos.exception.ProptechOsServiceException;
import java.util.UUID;

public interface IHttpClient<T> {

  T getById(Class<T> clazz, String endpoint, UUID uuid) throws ProptechOsServiceException;

}
