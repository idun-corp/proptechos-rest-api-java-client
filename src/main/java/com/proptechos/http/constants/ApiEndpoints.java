package com.proptechos.http.constants;

public interface ApiEndpoints {

  String PROPERTY_OWNER_JSON = "/json/propertyowner";
  String REAL_ESTATE_JSON = "/json/realestate";
  String REAL_ESTATE_COMPONENT_JSON = "/json/realestatecomponent";
  String BUILDING_COMPONENT_JSON = "/json/buildingcomponent";
  String STOREY_JSON = "/json/storey";
  String ROOM_JSON = "/json/room";
  String ASSET_JSON = "/json/asset";
  String DEVICE_JSON = "/json/device";
  String ACTUATOR_JSON = "/json/actuator";
  String ACTUATION_JSON = "/json/actuator/%s/actuation";
  String SENSOR_JSON = "/json/sensor";
  String OBSERVATION_JSON = "/sensor/%s/observation";
  String LATEST_OBSERVATION_JSON = "/sensor/%s/observation/latest";
  String LATEST_OBSERVATIONS_JSON = "/sensor/observations/latest";
  String ACTUATION_INTERFACE_JSON = "/json/actuationinterface";
  String ALIAS_NAMESPACE_JSON = "/json/aliasnamespace";
  String COLLECTION_JSON = "/json/collection";

  String DEVICE_FUNCTION_JSON = "/json/devicefunction";
  String MEASUREMENT_UNIT_JSON = "/json/measurementunit";
  String PLACEMENT_CONTEXT_JSON = "/json/placementcontext";
  String QUANTITY_KIND_JSON = "/json/quantitykind";
  String BUILDING_COMPONENT_CLASS_JSON = "/json/buildingcomponentclass";
  String ROOM_TYPE_JSON = "/json/roomtype";

  String DEVICE_JSON_LD = "/device";

}
