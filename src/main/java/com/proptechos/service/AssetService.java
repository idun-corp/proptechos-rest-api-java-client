package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Asset;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.ASSETS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ASSET_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

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

    public BatchResponse<Asset> createAssets(List<Asset> assets) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, ASSETS_JSON, assets);
    }

    public BatchResponse<Asset> updateAssets(List<Asset> assets) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, ASSETS_JSON, assets);
    }

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(ASSET_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

}
