package com.proptechos.utils;

import com.proptechos.model.Observation;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class ObservationSerde implements Serde<Observation> {

    private final Deserializer<Observation> deserializer;

    public ObservationSerde() {
        this.deserializer = new ObservationDeserializer();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        deserializer.close();
    }

    @Override
    public Serializer<Observation> serializer() {
        return null;
    }

    @Override
    public Deserializer<Observation> deserializer() {
        return deserializer;
    }
}
