package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.SENSOR_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.Sensor;
import java.util.UUID;

public class SensorService extends PagedService<Sensor> {
  
  SensorService(String baseAppUrl) {
    super(baseAppUrl, SENSOR_JSON);
  }

  public Sensor getById(UUID id) throws ProptechOsServiceException {
    return httpClient.getById(Sensor.class, SENSOR_JSON, id);
  }

  public Sensor createSensor(Sensor sensor) throws ProptechOsServiceException {
    return httpClient.createObject(Sensor.class, SENSOR_JSON, sensor);
  }

  public Sensor updateSensor(Sensor sensor) throws ProptechOsServiceException {
    return httpClient.updateObject(Sensor.class, SENSOR_JSON, sensor);
  }

  public void deleteSensor(UUID id) throws ProptechOsServiceException {
    httpClient.deleteObject(SENSOR_JSON, id);
  }

}
