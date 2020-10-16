package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.REAL_ESTATE_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Point;
import com.proptechos.model.RealEstate;
import com.proptechos.service.filters.DistanceFilter;
import com.proptechos.service.filters.LatitudeFilter;
import com.proptechos.service.filters.LongitudeFilter;
import java.util.List;
import java.util.UUID;

public class RealEstateService extends PagedService<RealEstate> {
  
  RealEstateService(String baseAppUrl) {
    super(baseAppUrl, REAL_ESTATE_JSON);
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
    return httpClient.createObject(RealEstate.class, REAL_ESTATE_JSON, realEstate);
  }

  public RealEstate updateRealEstate(RealEstate realEstate) throws ProptechOsServiceException {
    return httpClient.updateObject(RealEstate.class, REAL_ESTATE_JSON, realEstate);
  }

  public void deleteRealEstate(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(REAL_ESTATE_JSON, id);
  }

}
