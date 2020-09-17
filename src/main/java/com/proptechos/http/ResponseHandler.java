package com.proptechos.http;

import static com.proptechos.http.constants.HttpStatus.BAD_GATEWAY;
import static com.proptechos.http.constants.HttpStatus.BAD_REQUEST;
import static com.proptechos.http.constants.HttpStatus.FORBIDDEN;
import static com.proptechos.http.constants.HttpStatus.GATEWAY_TIMEOUT;
import static com.proptechos.http.constants.HttpStatus.INTERNAL_SERVER_ERROR;
import static com.proptechos.http.constants.HttpStatus.NOT_ACCEPTABLE;
import static com.proptechos.http.constants.HttpStatus.NOT_FOUND;
import static com.proptechos.http.constants.HttpStatus.OK;
import static com.proptechos.http.constants.HttpStatus.SERVICE_UNAVAILABLE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proptechos.exception.EntityNotFoundException;
import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.exception.ServiceAccessDeniedException;
import com.proptechos.exception.ServiceInvalidUsageException;
import com.proptechos.exception.ServiceUnavailableException;
import com.proptechos.exception.TypeConvertException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class ResponseHandler {

  private final ObjectMapper mapper;

  public ResponseHandler() {
    this.mapper = new ObjectMapper();
  }

  public <T> T handleResponse(Class<T> clazz, CloseableHttpResponse httpResponse)
      throws ProptechOsServiceException {
    int status = httpResponse.getStatusLine().getStatusCode();
    String stringEntity = getStringEntity(httpResponse);
    if (status == OK) {
      return parseSingleEntity(clazz, stringEntity);
    }
    String errorMsg = getErrorMsg(stringEntity);
    switch (status) {
      case FORBIDDEN:
        throw new ServiceAccessDeniedException(errorMsg);
      case NOT_FOUND:
        throw new EntityNotFoundException(errorMsg);
      case BAD_REQUEST:
      case NOT_ACCEPTABLE:
        throw new ServiceInvalidUsageException(errorMsg);
      case INTERNAL_SERVER_ERROR:
        throw new ServiceUnavailableException(errorMsg);
      case BAD_GATEWAY:
      case SERVICE_UNAVAILABLE:
      case GATEWAY_TIMEOUT:
        throw new ServiceUnavailableException("Service unavailable." + errorMsg);
    }
    throw new ServiceUnavailableException("Service unavailable. Please try later.");
  }

  private String getStringEntity(CloseableHttpResponse httpResponse)
      throws ProptechOsServiceException {
    try {
      HttpEntity httpEntity = httpResponse.getEntity();
      return EntityUtils.toString(httpEntity);
    } catch (IOException e) {
      throw new ServiceInvalidUsageException(e.getMessage());
    }
  }

  private  <T> T parseSingleEntity(Class<T> clazz, String entity)
      throws ProptechOsServiceException {
    try {
      return mapper.readValue(entity, clazz);
    } catch (IOException e) {
      throw new TypeConvertException(
          "Failed to convert ProptechOS service response to datatype " + clazz.getName(), e);
    }
  }

  private String getErrorMsg(String errorEntity) {
    try {
      JsonNode error = mapper.readTree(errorEntity);
      return error.path("message").toString();
    } catch (IOException e) {
      return e.getMessage();
    }
  }

}
