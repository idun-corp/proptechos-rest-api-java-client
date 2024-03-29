package com.proptechos.service;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.BatchResponse;
import com.proptechos.model.buildingcomponent.Room;
import com.proptechos.model.history.AxiomSnapshot;
import com.proptechos.service.filters.device.EndTimeFilter;
import com.proptechos.service.filters.device.StartTimeFilter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.proptechos.http.constants.ApiEndpoints.ROOMS_JSON;
import static com.proptechos.http.constants.ApiEndpoints.ROOM_JSON;
import static com.proptechos.utils.ClientUtils.getHistoryEndpoint;

public class RoomService extends PagedService<Room> {

    RoomService(String baseAppUrl) {
        super(baseAppUrl, ROOM_JSON, Room.class);
    }

    public Room getById(UUID id) throws ProptechOsServiceException {
        return httpClient.getById(typeClazz, ROOM_JSON, id);
    }

    public Room createRoom(Room room) throws ProptechOsServiceException {
        return httpClient.createObject(typeClazz, ROOM_JSON, room);
    }

    public Room updateRoom(Room room) throws ProptechOsServiceException {
        return httpClient.updateObject(typeClazz, ROOM_JSON, room);
    }

    public void deleteRoom(UUID id) throws ProptechOsServiceException {
        httpClient.deleteObject(ROOM_JSON, id);
    }

    public BatchResponse<Room> createRooms(List<Room> rooms) throws ProptechOsServiceException {
        return httpClient.createBatch(typeClazz, ROOMS_JSON, rooms);
    }

    public BatchResponse<Room> updateRooms(List<Room> rooms) throws ProptechOsServiceException {
        return httpClient.updateBatch(typeClazz, ROOMS_JSON, rooms);
    }

    public List<AxiomSnapshot> getHistory(UUID id, Instant startTime, Instant endTime) {
        return httpClient.getList(AxiomSnapshot.class, getHistoryEndpoint(ROOM_JSON, id),
            StartTimeFilter.getInstance(startTime),
            EndTimeFilter.getInstance(endTime));
    }

}
