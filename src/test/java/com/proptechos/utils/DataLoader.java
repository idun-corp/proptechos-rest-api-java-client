package com.proptechos.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class DataLoader {

  public static String loadPagedDevices() {
    String dataFile = "data/device/paged.json";
    InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(dataFile);
    return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  public static String loadSingleDevice() {
    String dataFile = "data/device/single.json";
    InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(dataFile);
    return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
  }

}
