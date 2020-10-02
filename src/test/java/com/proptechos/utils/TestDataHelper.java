package com.proptechos.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proptechos.model.Device;
import com.proptechos.model.common.IDevice;
import java.util.UUID;

public class TestDataHelper {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static IDevice buildDevice() {
    Device device = new Device();
    device.setPopularName("Test device");
    device.setLittera("Test littera");
    device.setIsMountedInBuildingComponent(UUID.randomUUID());
    return device;
  }

  public static String objectToJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

}
