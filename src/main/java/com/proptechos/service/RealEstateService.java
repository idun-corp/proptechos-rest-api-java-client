package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.Point;
import com.proptechos.model.RealEstate;
import com.proptechos.service.filters.location.DistanceFilter;
import com.proptechos.service.filters.location.LatitudeFilter;
import com.proptechos.service.filters.location.LongitudeFilter;

import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.*;

public class RealEstateService extends PagedService<RealEstate> {

    RealEstateService(String baseAppUrl) {
        super(baseAppUrl, REAL_ESTATE_JSON, RealEstate.class);
    }

    public RealEstate getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(RealEstate.class, REAL_ESTATE_JSON, id);
    }

    public List<RealEstate> getRealEstatesInRange(Point point) {
        return httpClient.getList(RealEstate.class, REAL_ESTATE_JSON + "/inrange",
            new LatitudeFilter(point.getLatitude()),
            new LongitudeFilter(point.getLongitude()),
            new DistanceFilter(point.getDistance()));
    }

    public RealEstate createRealEstate(RealEstate realEstate) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, REAL_ESTATE_JSON, realEstate);
    }

    public RealEstate updateRealEstate(RealEstate realEstate) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, REAL_ESTATE_JSON, realEstate);
    }

    public void deleteRealEstate(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(REAL_ESTATE_JSON, id);
    }

    public BatchResponse<RealEstate> createRealEstates(List<RealEstate> realEstates) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, REAL_ESTATES_JSON, realEstates);
    }

    public BatchResponse<RealEstate> updateRealEstates(List<RealEstate> realEstates) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, REAL_ESTATES_JSON, realEstates);
    }

}
