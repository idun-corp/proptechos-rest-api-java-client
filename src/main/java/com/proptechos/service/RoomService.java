package com.proptechos.service;

import static com.proptechos.http.constants.ApiEndpoints.ROOM_JSON;

import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.buildingcomponent.Room;
import java.util.UUID;

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

}
