package com.proptechos.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proptechos.model.Observation;
import java.io.IOException;
import java.util.Objects;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class ObservationDeserializer implements Deserializer<Observation> {

  private final ObjectMapper mapper = new ObjectMapper();
  private final String encoding = "UTF8";

  @Override
  public Observation deserialize(String s, byte[] data) {
    if (Objects.isNull(data)) {
      return null;
    }
    try {
      String observationJson = mapper.readTree(new String(data, this.encoding))
              .path("observation").toString();
      return mapper.readValue(observationJson, Observation.class);
    } catch (IOException var4) {
      throw new SerializationException("Error when deserializing byte[] to string due to unsupported encoding " + this.encoding);
    }
  }
}
