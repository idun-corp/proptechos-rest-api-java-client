package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ASSET_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Asset;
import java.util.UUID;

public class AssetService extends PagedService<Asset> {
  
  AssetService(String baseAppUrl) {
    super(baseAppUrl, ASSET_JSON, Asset.class);
  }

  public Asset getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(Asset.class, ASSET_JSON, id);
  }

  public Asset createAsset(Asset asset) throws ProptechOsServiceException {
    return httpClient.createObject(typeClazz, ASSET_JSON, asset);
  }

  public Asset updateAsset(Asset asset) throws ProptechOsServiceException {
    return httpClient.updateObject(typeClazz, ASSET_JSON, asset);
  }

  public void deleteAsset(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(ASSET_JSON, id);
  }

}
