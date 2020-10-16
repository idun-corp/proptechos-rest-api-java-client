package com.proptechos.model;

public class Point {

  private final double latitude;
  private final double longitude;
  private final double distance;

  public Point(double latitude, double longitude, double distance) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.distance = distance;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getDistance() {
    return distance;
  }
}
