package com.proptechos.model.history;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.proptechos.util.InstantDeserializer;
import com.proptechos.util.InstantSerializer;

import java.time.Instant;

public class AxiomSnapshot {

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant snapshotTime;

    private Operation operation;

    private String agent;

    private Object snapshot;

    public Instant getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(Instant snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Object getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Object snapshot) {
        this.snapshot = snapshot;
    }
}
