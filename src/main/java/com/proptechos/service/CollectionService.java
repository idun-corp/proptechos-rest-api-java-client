package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Collection;
import com.proptechos.model.common.IBaseClass;
import com.proptechos.model.common.Paged;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.common.PageNumberFilter;
import com.proptechos.service.filters.common.PageSizeFilter;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.COLLECTION_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

public class CollectionService extends PagedService<Collection> {

    CollectionService(String baseAppUrl) {
        super(baseAppUrl, COLLECTION_JSON, Collection.class);
    }

    public Collection getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(Collection.class, COLLECTION_JSON, id);
    }

    public Collection createCollection(Collection collection) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, COLLECTION_JSON, collection);
    }

    public Collection updateCollection(Collection collection) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, COLLECTION_JSON, collection);
    }

    public List<UUID> includeAxioms(UUID collectionId, UUID... axiomIds) {
        List<UUID> providedIds =
            axiomIds == null ? new ArrayList<>() : Arrays.asList(axiomIds);
        return httpClient.putList(UUID.class,
            COLLECTION_JSON + "/" + collectionId + "/include",
            providedIds);
    }

    public List<UUID> excludeAxioms(UUID collectionId, UUID... axiomIds) {
        List<UUID> providedIds =
            axiomIds == null ? new ArrayList<>() : Arrays.asList(axiomIds);
        return httpClient.putList(UUID.class,
            COLLECTION_JSON + "/" + collectionId + "/exclude",
            providedIds);
    }

    public Paged<IBaseClass> getIncludedAxioms(UUID collectionId, long pageNumber, long pageSize) {
        validatePageMetadata(pageNumber, pageSize);
        return httpClient.getPaged(IBaseClass.class,
            COLLECTION_JSON + "/" + collectionId + "/includes",
            new PageSizeFilter(pageSize), new PageNumberFilter(pageNumber));
    }

    public void deleteCollection(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(COLLECTION_JSON, id);
    }

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(COLLECTION_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

}
