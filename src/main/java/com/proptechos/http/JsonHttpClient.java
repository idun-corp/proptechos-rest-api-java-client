package com.proptechos.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.exception.ServiceInvalidUsageException;
import com.proptechos.http.header.AcceptJsonHeader;
import com.proptechos.http.header.ContentJsonHeader;
import com.proptechos.http.header.HeaderManager;
import com.proptechos.http.header.OAuth2TokenHeader;
import com.proptechos.http.header.PropertyOwnerHeader;
import com.proptechos.http.query.IQueryFilter;
import com.proptechos.http.query.QueryBuilder;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.common.Paged;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class JsonHttpClient {

    private static volatile JsonHttpClient INSTANCE;
    private static final Object lock = new Object();

    private final String baseApiUrl;
    private final ResponseHandler responseHandler;
    private final ObjectMapper mapper;

    private JsonHttpClient(String baseApiUrl) {
        this.baseApiUrl = baseApiUrl;
        this.responseHandler = new ResponseHandler();
        this.mapper = new ObjectMapper();
    }

    public static JsonHttpClient getInstance(String baseApiUrl) {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new JsonHttpClient(baseApiUrl);
                }
            }
        }

        return INSTANCE;
    }

    public <T> T getById(Class<T> clazz, String endpoint, UUID uuid, IQueryFilter... queryFilters) throws ProptechOsServiceException {
        return getSingle(clazz, endpoint + "/" + uuid, queryFilters);
    }

    public <T> T getSingle(Class<T> clazz, String endpoint, IQueryFilter... queryFilters) throws ProptechOsServiceException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(
                httpGet(endpoint + buildQueryParams(queryFilters)));
            return responseHandler.handleResponse(clazz, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public <T> Paged<T> getPaged(Class<T> clazz, String endpoint, IQueryFilter... queryFilters) throws ProptechOsServiceException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response =
                client.execute(httpGet(endpoint + buildQueryParams(queryFilters)));
            return responseHandler.handlePagedResponse(clazz, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public <T> List<T> getList(Class<T> clazz, String endpoint, IQueryFilter... queryFilters)
        throws ProptechOsServiceException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response =
                client.execute(httpGet(endpoint + buildQueryParams(queryFilters)));
            return responseHandler.handleListResponse(clazz, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public <K, V> Map<K, V> postRequestObject(Class<K> keyClass, Class<V> valueClass,
                                              String endpoint, Object object) throws ProptechOsServiceException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response =
                client.execute(httpPost(endpoint, object));
            return responseHandler.handleMapResponse(keyClass, valueClass, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public <T> T createObject(Class<T> clazz, String endpoint, Object object) throws ProptechOsServiceException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(httpPost(endpoint, object));
            return responseHandler.handleResponse(clazz, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public <T> T updateObject(Class<T> clazz, String endpoint, Object object) throws ProptechOsServiceException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(httpPut(endpoint, object));
            return responseHandler.handleResponse(clazz, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public void deleteObject(String endpoint, UUID uuid) throws ProptechOsServiceException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(httpDelete(endpoint + "/" + uuid));
            responseHandler.handleResponse(Object.class, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public <T> List<T> putList(Class<T> clazz, String endpoint, Object object) throws ProptechOsServiceException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(httpPut(endpoint, object));
            return responseHandler.handleListResponse(clazz, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public <T> BatchResponse<T> createBatch(Class<T> clazz, String endpoint, List<T> data) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(httpPost(endpoint, data));
            return responseHandler.handleBatchResponse(clazz, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    public <T> BatchResponse<T> updateBatch(Class<T> clazz, String endpoint, List<T> data) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(httpPut(endpoint, data));
            return responseHandler.handleBatchResponse(clazz, response);
        } catch (IOException e) {
            throw new ServiceInvalidUsageException(e.getMessage(), e);
        }
    }

    private HttpGet httpGet(String url) {
        HeaderManager headerManager = HeaderManager.getInstance(
            new AcceptJsonHeader(), new OAuth2TokenHeader(), new PropertyOwnerHeader());
        HttpGet httpGet = new HttpGet(baseApiUrl + url);
        headerManager.addHeaders(httpGet);
        return httpGet;
    }

    private <T> HttpPost httpPost(String url, T body) throws ProptechOsServiceException {
        try {
            HeaderManager headerManager = HeaderManager.getInstance(
                new AcceptJsonHeader(), new ContentJsonHeader(), new OAuth2TokenHeader(), new PropertyOwnerHeader());
            HttpPost httpPost = new HttpPost(baseApiUrl + url);
            StringEntity modelData = new StringEntity(mapper.writeValueAsString(body));
            httpPost.setEntity(modelData);
            headerManager.addHeaders(httpPost);
            return httpPost;
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new ServiceInvalidUsageException("Unable serialize entity as json");
        }
    }

    private <T> HttpPut httpPut(String url, T body) throws ProptechOsServiceException {
        try {
            HeaderManager headerManager = HeaderManager.getInstance(
                new AcceptJsonHeader(), new ContentJsonHeader(), new OAuth2TokenHeader(), new PropertyOwnerHeader());
            HttpPut httpPut = new HttpPut(baseApiUrl + url);
            StringEntity modelData = new StringEntity(mapper.writeValueAsString(body));
            httpPut.setEntity(modelData);
            headerManager.addHeaders(httpPut);
            return httpPut;
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new ServiceInvalidUsageException("Unable serialize entity as json");
        }
    }

    private HttpDelete httpDelete(String url) {
        HeaderManager headerManager = HeaderManager.getInstance(
            new AcceptJsonHeader(), new OAuth2TokenHeader(), new PropertyOwnerHeader());
        HttpDelete httpDelete = new HttpDelete(baseApiUrl + url);
        headerManager.addHeaders(httpDelete);
        return httpDelete;
    }

    private String buildQueryParams(IQueryFilter... queryFilters) {
        QueryBuilder builder = QueryBuilder.builder();
        for (IQueryFilter filter : queryFilters) {
            if (filter != null) {
                builder.append(filter.queryParam());
            }
        }
        return builder.build();
    }

}
