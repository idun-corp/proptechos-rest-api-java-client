package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.*;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.proptechos.http.constants.ApiEndpoints.*;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

public class SensorService extends PagedService<Sensor> {

    SensorService(String baseAppUrl) {
        super(baseAppUrl, SENSOR_JSON, Sensor.class);
    }

    public Sensor getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(typeClazz, SENSOR_JSON, id);
    }

    public Sensor createSensor(Sensor sensor) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, SENSOR_JSON, sensor);
    }

    public Sensor updateSensor(Sensor sensor) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, SENSOR_JSON, sensor);
    }

    public void deleteSensor(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(SENSOR_JSON, id);
    }

    public BatchResponse<Sensor> createSensors(List<Sensor> sensors) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, SENSORS_JSON, sensors);
    }

    public BatchResponse<Sensor> updateSensors(List<Sensor> sensors) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, SENSORS_JSON, sensors);
    }

    public Observation getLatestObservationBySensorId(UUID sensorId) {
        return httpClient.getSingle(Observation.class, String.format(LATEST_OBSERVATION_JSON, sensorId));
    }

    public List<Observation> getObservationsBySensorIdForPeriod(
        UUID sensorId, Instant startTime, Instant endTime) {
        return httpClient.getList(Observation.class, String.format(OBSERVATION_JSON, sensorId),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

    public Map<UUID, Observation> getLatestObservationsBySensorIds(List<UUID> sensorIds) {
        LatestObservationsRequest request = new LatestObservationsRequest();
        request.setSensorIds(sensorIds.stream().map(UUID::toString).collect(Collectors.toList()));
        return httpClient.postRequestObject(
            UUID.class, Observation.class, LATEST_OBSERVATIONS_JSON, request);
    }

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(SENSOR_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

}
