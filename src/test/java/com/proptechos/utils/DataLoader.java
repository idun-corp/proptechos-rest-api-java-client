package com.proptechos.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class DataLoader {

    public static String loadPagedPropertyOwners() {
        return loadData("data/propertyowner/paged.json");
    }

    public static String loadSinglePropertyOwner() {
        return loadData("data/propertyowner/single.json");
    }

    public static String loadPagedRealEstates() {
        return loadData("data/realestate/paged.json");
    }

    public static String loadRealEstateList() {
        return loadData("data/realestate/list.json");
    }

    public static String loadSingleRealEstate() {
        return loadData("data/realestate/single.json");
    }

    public static String loadBatchFailedRealEstates() {
        return loadData("data/realestate/batchError.json");
    }

    public static String loadPagedBuildings() {
        return loadData("data/building/paged.json");
    }

    public static String loadBuildingList() {
        return loadData("data/building/list.json");
    }

    public static String loadSingleBuilding() {
        return loadData("data/building/single.json");
    }

    public static String loadBatchFailedBuildings() {
        return loadData("data/building/batchError.json");
    }

    public static String loadPagedBuildingComponents() {
        return loadData("data/buildingcomponent/paged.json");
    }

    public static String loadSingleBuildingComponent() {
        return loadData("data/buildingcomponent/single.json");
    }

    public static String loadBatchFailedBuildingComponents() {
        return loadData("data/buildingcomponent/batchError.json");
    }

    public static String loadPagedStoreys() {
        return loadData("data/storey/paged.json");
    }

    public static String loadSingleStorey() {
        return loadData("data/storey/single.json");
    }

    public static String loadBatchFailedStoreys() {
        return loadData("data/storey/batchError.json");
    }

    public static String loadPagedRooms() {
        return loadData("data/room/paged.json");
    }

    public static String loadSingleRoom() {
        return loadData("data/room/single.json");
    }

    public static String loadBatchFailedRooms() {
        return loadData("data/room/batchError.json");
    }

    public static String loadPagedAssets() {
        return loadData("data/asset/paged.json");
    }

    public static String loadSingleAsset() {
        return loadData("data/asset/single.json");
    }

    public static String loadBatchFailedAssets() {
        return loadData("data/asset/batchError.json");
    }

    public static String loadPagedDevices() {
        return loadData("data/device/paged.json");
    }

    public static String loadSingleDevice() {
        return loadData("data/device/single.json");
    }

    public static String loadBatchFailedDevices() {
        return loadData("data/device/batchError.json");
    }

    public static String loadPagedActuators() {
        return loadData("data/actuator/paged.json");
    }

    public static String loadSingleActuator() {
        return loadData("data/actuator/single.json");
    }

    public static String loadActuationResponse() {
        return loadData("data/actuator/actuationResponse.json");
    }

    public static String loadBatchFailedActuators() {
        return loadData("data/actuator/batchError.json");
    }

    public static String loadPagedSensors() {
        return loadData("data/sensor/paged.json");
    }

    public static String loadSingleSensor() {
        return loadData("data/sensor/single.json");
    }

    public static String loadBatchFailedSensors() {
        return loadData("data/sensor/batchError.json");
    }

    public static String loadPagedActuationInterfaces() {
        return loadData("data/actuationinterface/paged.json");
    }

    public static String loadSingleActuationInterface() {
        return loadData("data/actuationinterface/single.json");
    }

    public static String loadPagedAliasNamespaces() {
        return loadData("data/aliasnamespace/paged.json");
    }

    public static String loadSingleAliasNamespace() {
        return loadData("data/aliasnamespace/single.json");
    }

    public static String loadBatchFailedAliasNamespaces() {
        return loadData("data/aliasnamespace/batchError.json");
    }

    public static String loadPagedCollection() {
        return loadData("data/collection/paged.json");
    }

    public static String loadPagedSystem() {
        return loadData("data/system/paged.json");
    }

    public static String loadSingleCollection() {
        return loadData("data/collection/single.json");
    }

    public static String loadSingleSystem() {
        return loadData("data/system/single.json");
    }

    public static String loadDeviceFunctions() {
        return loadData("data/recindividuals/devicefunctiontypes.json");
    }

    public static String loadPlacementContexts() {
        return loadData("data/recindividuals/placementcontexts.json");
    }

    public static String loadMeasurementUnits() {
        return loadData("data/recindividuals/measurementunits.json");
    }

    public static String loadQuantityKinds() {
        return loadData("data/recindividuals/qauntitykinds.json");
    }

    public static String loadBuildingComponentClasses() {
        return loadData("data/recindividuals/buildingcomponentclasses.json");
    }

    public static String loadRoomTypes() {
        return loadData("data/recindividuals/roomtypes.json");
    }

    private static String loadData(String dataFile) {
        InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(dataFile);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"));
    }

}
