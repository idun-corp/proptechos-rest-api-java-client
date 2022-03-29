package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.common.IDevice;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.*;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

public class DeviceService extends PagedService<IDevice> {

    DeviceService(String baseAppUrl) {
        super(baseAppUrl, DEVICE_JSON, IDevice.class);
    }

    public IDevice getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(typeClazz, DEVICE_JSON, id);
    }

    public IDevice createDevice(IDevice device) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, DEVICE_JSON, device);
    }

    public IDevice updateDevice(IDevice device) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, DEVICE_JSON, device);
    }

    public void deleteDevice(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(DEVICE_JSON, id);
    }

    public BatchResponse<IDevice> createDevices(List<IDevice> devices) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, DEVICES_JSON, devices);
    }

    public BatchResponse<IDevice> updateDevices(List<IDevice> devices) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, DEVICES_JSON, devices);
    }

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(DEVICE_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

}
