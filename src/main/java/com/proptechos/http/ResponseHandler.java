package com.proptechos.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.proptechos.exception.EntityNotFoundException;
import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.exception.ServiceAccessDeniedException;
import com.proptechos.exception.ServiceInvalidUsageException;
import com.proptechos.exception.ServiceUnavailableException;
import com.proptechos.exception.TypeConvertException;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.common.IBaseClass;
import com.proptechos.model.common.Paged;
import com.proptechos.utils.BaseClassMixin;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import static com.proptechos.http.constants.HttpStatus.*;

public class ResponseHandler {

    private final ObjectMapper mapper;

    public ResponseHandler() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.addMixIn(IBaseClass.class, BaseClassMixin.class);
    }

    public <T> T handleResponse(Class<T> clazz, CloseableHttpResponse httpResponse)
        throws ProptechOsServiceException {
        int status = httpResponse.getStatusLine().getStatusCode();
        String stringEntity =
            Objects.nonNull(httpResponse.getFirstHeader("content-type")) ?
                getStringEntity(httpResponse) : "{}";
        if (status == OK) {
            return parseSingleEntity(clazz, stringEntity);
        }
        handleNonSuccessResponse(status, stringEntity);
        throw new ServiceUnavailableException("Service unavailable. Please try later.");
    }

    public <T> Paged<T> handlePagedResponse(Class<T> clazz, CloseableHttpResponse httpResponse)
        throws ProptechOsServiceException {
        int status = httpResponse.getStatusLine().getStatusCode();
        String stringEntity =
            Objects.nonNull(httpResponse.getFirstHeader("content-type")) ?
                getStringEntity(httpResponse) : "{}";
        if (status == OK) {
            return parsePagedEntityResponse(clazz, stringEntity);
        }
        handleNonSuccessResponse(status, stringEntity);
        throw new ServiceUnavailableException("Service unavailable. Please try later.");
    }

    public <T> List<T> handleListResponse(Class<T> clazz, CloseableHttpResponse httpResponse)
        throws ProptechOsServiceException {
        int status = httpResponse.getStatusLine().getStatusCode();
        String stringEntity =
            Objects.nonNull(httpResponse.getFirstHeader("content-type")) ?
                getStringEntity(httpResponse) : "[]";
        if (status == OK) {
            return parseEntityList(clazz, stringEntity);
        }
        handleNonSuccessResponse(status, stringEntity);
        throw new ServiceUnavailableException("Service unavailable. Please try later.");
    }

    public <K, V> Map<K, V> handleMapResponse(Class<K> keyClass, Class<V> valueClass,
                                              CloseableHttpResponse httpResponse) throws ProptechOsServiceException {
        int status = httpResponse.getStatusLine().getStatusCode();
        String stringEntity =
            Objects.nonNull(httpResponse.getFirstHeader("content-type")) ?
                getStringEntity(httpResponse) : "{}";
        if (status == OK) {
            return parseEntityMap(keyClass, valueClass, stringEntity);
        }
        handleNonSuccessResponse(status, stringEntity);
        throw new ServiceUnavailableException("Service unavailable. Please try later.");
    }

    public <T> BatchResponse<T> handleBatchResponse(Class<T> clazz, CloseableHttpResponse httpResponse)
        throws ProptechOsServiceException {
        int status = httpResponse.getStatusLine().getStatusCode();
        String stringEntity =
            Objects.nonNull(httpResponse.getFirstHeader("content-type")) ?
                getStringEntity(httpResponse) : "{}";
        if (status == OK || status == MULTI_STATUS) {
            return parseBatchResponse(clazz, stringEntity);
        }
        if (status == BAD_REQUEST) {
            try {
                return parseBatchResponse(clazz, stringEntity);
            } catch (TypeConvertException e) {
                //do nothing let handleNonSuccessResponse
            }
        }
        handleNonSuccessResponse(status, stringEntity);
        throw new ServiceUnavailableException("Service unavailable. Please try later.");
    }

    private void handleNonSuccessResponse(int status, String stringEntity) {
        String errorMsg = getErrorMsg(stringEntity);
        switch (status) {
            case UNAUTHORIZED:
            case FORBIDDEN:
                throw new ServiceAccessDeniedException(errorMsg);
            case NOT_FOUND:
                throw new EntityNotFoundException(errorMsg);
            case BAD_REQUEST:
            case NOT_ACCEPTABLE:
            case CONFLICT:
                throw new ServiceInvalidUsageException(errorMsg);
            case INTERNAL_SERVER_ERROR:
                throw new ServiceUnavailableException(errorMsg);
            case BAD_GATEWAY:
            case SERVICE_UNAVAILABLE:
            case GATEWAY_TIMEOUT:
                throw new ServiceUnavailableException("Service unavailable." + errorMsg);
        }
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

    private <T> T parseSingleEntity(Class<T> clazz, String entity)
        throws ProptechOsServiceException {
        try {
            return mapper.readValue(entity, clazz);
        } catch (IOException e) {
            throw new TypeConvertException(
                "Failed to convert ProptechOS service response to datatype " + clazz.getName(), e);
        }
    }

    private <T> Paged<T> parsePagedEntityResponse(Class<T> clazz, String entity)
        throws ProptechOsServiceException {
        try {
            JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(Paged.class, clazz);
            return mapper.readValue(entity, javaType);
        } catch (IOException e) {
            throw new TypeConvertException(
                "Failed to convert ProptechOS service response to datatype " + clazz.getName(), e);
        }
    }

    private <T> List<T> parseEntityList(Class<T> clazz, String entities) {
        try {
            JavaType entityType = mapper.constructType(clazz);
            return mapper.readValue(entities,
                mapper.getTypeFactory().constructCollectionType(List.class, entityType));
        } catch (IOException e) {
            throw new TypeConvertException(
                "Failed to convert ProptechOS service collection response to datatype " + clazz.getName(), e);
        }
    }

    private <K, V> Map<K, V> parseEntityMap(Class<K> keyClass, Class<V> valueClass, String entities) {
        try {
            JavaType keyType = mapper.constructType(keyClass);
            JavaType valueType = mapper.constructType(valueClass);
            return mapper.readValue(entities,
                mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType));
        } catch (IOException e) {
            throw new TypeConvertException(
                "Failed to convert ProptechOS service map response to datatype " + valueClass.getName(), e);
        }
    }

    private <T> BatchResponse<T> parseBatchResponse(Class<T> clazz, String entities) {
        try {
            return mapper.readValue(entities,
                mapper.getTypeFactory().constructParametricType(BatchResponse.class, clazz));
        } catch (IOException e) {
            throw new TypeConvertException(
                "Failed to convert ProptechOS service collection response to datatype " + clazz.getName(), e);
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
