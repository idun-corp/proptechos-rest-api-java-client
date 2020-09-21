package com.proptechos.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.exception.ServiceInvalidUsageException;
import com.proptechos.http.header.AcceptJsonHeader;
import com.proptechos.http.header.ContentJsonHeader;
import com.proptechos.http.header.HeaderManager;
import com.proptechos.http.header.OAuth2TokenHeader;
import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryBuilder;
import com.proptechos.model.common.PageMetadata;
import com.proptechos.model.common.Paged;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class JsonHttpClient<T> implements IHttpClient<T> {

  private final CloseableHttpClient client;
  private final String baseApiUrl;
  private final ResponseHandler responseHandler;
  private final ObjectMapper mapper;

  public JsonHttpClient(String baseApiUrl) {
    this.client = HttpClientBuilder.create().build();
    this.baseApiUrl = baseApiUrl;
    this.responseHandler = new ResponseHandler();
    this.mapper = new ObjectMapper();
  }

  public T getById(Class<T> clazz, String endpoint, UUID uuid) throws ProptechOsServiceException {
    try {
      CloseableHttpResponse response = client.execute(httpGet(endpoint + "/" + uuid));
      return responseHandler.handleResponse(clazz, response);
    } catch (IOException e) {
      throw new ServiceInvalidUsageException(e.getMessage(), e);
    }
  }

  public Paged<T> getPaged(String endpoint, IQueryFilter...queryFilters) throws ProptechOsServiceException {
    try {
      CloseableHttpResponse response =
          client.execute(httpGet(endpoint + buildQueryParams(queryFilters)));
      return responseHandler.handleResponse(Paged.class, response);
    } catch (IOException e) {
      throw new ServiceInvalidUsageException(e.getMessage(), e);
    }
  }

  public T createObject(Class<T> clazz, String endpoint, T object) throws ProptechOsServiceException {
    try {
      CloseableHttpResponse response = client.execute(httpPost(endpoint, object));
      return responseHandler.handleResponse(clazz, response);
    } catch (IOException e) {
      throw new ServiceInvalidUsageException(e.getMessage(), e);
    }
  }

  public T updateObject(Class<T> clazz, String endpoint, T object) throws ProptechOsServiceException {
    try {
      CloseableHttpResponse response = client.execute(httpPut(endpoint, object));
      return responseHandler.handleResponse(clazz, response);
    } catch (IOException e) {
      throw new ServiceInvalidUsageException(e.getMessage(), e);
    }
  }

  private HttpGet httpGet(String url) {
    HeaderManager headerManager = HeaderManager.getInstance(
        new AcceptJsonHeader(), new OAuth2TokenHeader());
    HttpGet httpGet = new HttpGet(baseApiUrl + url);
    headerManager.addHeaders(httpGet);
    return httpGet;
  }

  private HttpPost httpPost(String url, T body) throws ProptechOsServiceException {
    try {
      HeaderManager headerManager = HeaderManager.getInstance(
          new AcceptJsonHeader(), new ContentJsonHeader(), new OAuth2TokenHeader());
      HttpPost httpPost = new HttpPost(baseApiUrl + url);
      StringEntity modelData = new StringEntity(mapper.writeValueAsString(body));
      httpPost.setEntity(modelData);
      headerManager.addHeaders(httpPost);
      return httpPost;
    } catch (JsonProcessingException | UnsupportedEncodingException e) {
      throw new ServiceInvalidUsageException("Unable serialize entity as json");
    }
  }

  private HttpPut httpPut(String url, T body) throws ProptechOsServiceException {
    try {
      HeaderManager headerManager = HeaderManager.getInstance(
          new AcceptJsonHeader(), new ContentJsonHeader(), new OAuth2TokenHeader());
      HttpPut httpPut = new HttpPut(baseApiUrl + url);
      StringEntity modelData = new StringEntity(mapper.writeValueAsString(body));
      httpPut.setEntity(modelData);
      headerManager.addHeaders(httpPut);
      return httpPut;
    } catch (JsonProcessingException | UnsupportedEncodingException e) {
      throw new ServiceInvalidUsageException("Unable serialize entity as json");
    }
  }

  private String buildQueryParams(IQueryFilter...queryFilters) {
    QueryBuilder builder = QueryBuilder.builder();
    for (IQueryFilter filter : queryFilters) {
      builder.append(filter.queryParam());
    }
    return builder.build();
  }

}
