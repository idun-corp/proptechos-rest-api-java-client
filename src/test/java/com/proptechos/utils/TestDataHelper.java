package com.proptechos.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proptechos.model.Actuator;
import com.proptechos.model.AliasNamespace;
import com.proptechos.model.Asset;
import com.proptechos.model.Building;
import com.proptechos.model.Collection;
import com.proptechos.model.Device;
import com.proptechos.model.RealEstate;
import com.proptechos.model.Sensor;
import com.proptechos.model.actuation.ActuationInterface;
import com.proptechos.model.actuation.DataType;
import com.proptechos.model.actuation.KeyValueDefinition;
import com.proptechos.model.actuation.RestrictionType;
import com.proptechos.model.buildingcomponent.Room;
import com.proptechos.model.buildingcomponent.Storey;
import com.proptechos.model.buildingcomponent.VirtualBuildingComponent;
import com.proptechos.model.common.IBuildingComponent;
import com.proptechos.model.common.IDevice;
import java.util.UUID;

public class TestDataHelper {

  private static final ObjectMapper mapper = new ObjectMapper();

  private static final String TEST_NAME = "Test axiom";
  private static final String TEST_LITTERA = "Test littera";

  public static RealEstate buildRealEstate() {
    RealEstate realEstate = new RealEstate();
    realEstate.setPopularName(TEST_NAME);
    realEstate.setRecClass("RealEstate");
    realEstate.setLittera(TEST_LITTERA);
    return realEstate;
  }

  public static Building buildBuilding() {
    Building building = new Building();
    building.setPopularName(TEST_NAME);
    building.setRecClass("Building");
    building.setLittera(TEST_LITTERA);
    building.setIsPartOfRealEstate(UUID.randomUUID());
    return building;
  }

  public static IBuildingComponent buildBuildingComponent() {
    VirtualBuildingComponent buildingComponent = new VirtualBuildingComponent();
    buildingComponent.setPopularName(TEST_NAME);
    buildingComponent.setRecClass("VirtualBuildingComponent");
    buildingComponent.setLittera(TEST_LITTERA);
    buildingComponent.setIsPartOfBuilding(UUID.randomUUID());
    return buildingComponent;
  }

  public static Room buildRoom() {
    Room room = new Room();
    room.setPopularName(TEST_NAME);
    room.setRecClass("Room");
    room.setLittera(TEST_LITTERA);
    room.setIsPartOfBuilding(UUID.randomUUID());
    return room;
  }

  public static Storey buildStorey() {
    Storey storey = new Storey();
    storey.setPopularName(TEST_NAME);
    storey.setRecClass("Storey");
    storey.setLittera(TEST_LITTERA);
    storey.setIsPartOfBuilding(UUID.randomUUID());
    return storey;
  }

  public static Asset buildAsset() {
    Asset asset = new Asset();
    asset.setPopularName(TEST_NAME);
    asset.setRecClass("Asset");
    asset.setLittera(TEST_LITTERA);
    asset.setLocatedIn(UUID.randomUUID());
    return asset;
  }

  public static IDevice buildDevice() {
    Device device = new Device();
    device.setPopularName(TEST_NAME);
    device.setRecClass("Device");
    device.setLittera(TEST_LITTERA);
    device.setIsMountedInBuildingComponent(UUID.randomUUID());
    return device;
  }

  public static Actuator buildActuator() {
    Actuator actuator = new Actuator();
    actuator.setPopularName(TEST_NAME);
    actuator.setRecClass("Actuator");
    actuator.setLittera(TEST_LITTERA);
    actuator.setIsMountedInBuildingComponent(UUID.randomUUID());
    actuator.setHasSuperDevice(UUID.randomUUID());
    actuator.setHasDefaultActuationInterface(UUID.randomUUID());
    return actuator;
  }

  public static Sensor buildSensor() {
    Sensor sensor = new Sensor();
    sensor.setPopularName(TEST_NAME);
    sensor.setLittera(TEST_LITTERA);
    sensor.setRecClass("Sensor");
    sensor.setIsMountedInBuildingComponent(UUID.randomUUID());
    sensor.setHasSuperDevice(UUID.randomUUID());
    return sensor;
  }

  public static AliasNamespace buildAliasNamespace() {
    AliasNamespace ns = new AliasNamespace();
    ns.setBaseURL("https://testns.idunrealestate.com/");
    ns.setRetrievable(true);
    ns.setPopularName(TEST_NAME);
    ns.setRecClass("AliasNamespace");
    return ns;
  }

  public static ActuationInterface buildActuationInterface() {
    ActuationInterface actuationInterface = new ActuationInterface();
    actuationInterface.setLittera(TEST_LITTERA);
    actuationInterface.setRecClass("ActuationInterface");
    KeyValueDefinition definition =
        ActuationInterfaceDataBuilder.builder()
            .dataType(DataType.Integer)
            .restriction(RestrictionType.Range, "1", "100").build();
    actuationInterface.setPayloadKeyValueDefinition(definition);
    return actuationInterface;
  }

  public static Collection buildCollection() {
    Collection collection = new Collection();
    collection.setPopularName(TEST_NAME);
    collection.setLittera(TEST_LITTERA);
    collection.setRecClass("Collection");
    return collection;
  }

  public static String objectToJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

}
