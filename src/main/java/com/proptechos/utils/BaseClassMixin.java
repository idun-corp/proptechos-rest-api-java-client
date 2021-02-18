package com.proptechos.utils;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.proptechos.model.Actuator;
import com.proptechos.model.AliasNamespace;
import com.proptechos.model.Asset;
import com.proptechos.model.Building;
import com.proptechos.model.Device;
import com.proptechos.model.RealEstate;
import com.proptechos.model.Sensor;
import com.proptechos.model.actuation.ActuationInterface;
import com.proptechos.model.buildingcomponent.Balcony;
import com.proptechos.model.buildingcomponent.BuildingComponent;
import com.proptechos.model.buildingcomponent.Facade;
import com.proptechos.model.buildingcomponent.Floor;
import com.proptechos.model.buildingcomponent.RoofInner;
import com.proptechos.model.buildingcomponent.RoofOuter;
import com.proptechos.model.buildingcomponent.Room;
import com.proptechos.model.buildingcomponent.Slab;
import com.proptechos.model.buildingcomponent.Storey;
import com.proptechos.model.buildingcomponent.Terrace;
import com.proptechos.model.buildingcomponent.VirtualBuildingComponent;
import com.proptechos.model.buildingcomponent.Wall;
import com.proptechos.model.buildingcomponent.Zone;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "class",
    visible = true)
@JsonSubTypes({
    @Type(value = RealEstate.class, name = "RealEstate"),
    @Type(value = Building.class, name = "Building"),
    @Type(value = BuildingComponent.class, name = "BuildingComponent"),
    @Type(value = Balcony.class, name = "Balcony"),
    @Type(value = Facade.class, name = "Facade"),
    @Type(value = Floor.class, name = "Floor"),
    @Type(value = RoofInner.class, name = "RoofInner"),
    @Type(value = RoofOuter.class, name = "RoofOuter"),
    @Type(value = Slab.class, name = "Slab"),
    @Type(value = Terrace.class, name = "Terrace"),
    @Type(value = Wall.class, name = "Wall"),
    @Type(value = Zone.class, name = "Zone"),
    @Type(value = Room.class, name = "Room"),
    @Type(value = Storey.class, name = "Storey"),
    @Type(value = VirtualBuildingComponent.class, name = "VirtualBuildingComponent"),
    @Type(value = Asset.class, name = "Asset"),
    @Type(value = Device.class, name = "Device"),
    @Type(value = Actuator.class, name = "Actuator"),
    @Type(value = Sensor.class, name = "Sensor"),
    @Type(value = AliasNamespace.class, name = "AliasNamespace"),
    @Type(value = ActuationInterface.class, name = "ActuationInterface")})
public class BaseClassMixin {

}
