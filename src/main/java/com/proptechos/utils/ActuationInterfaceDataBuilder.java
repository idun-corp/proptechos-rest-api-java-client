package com.proptechos.utils;

import com.proptechos.model.actuation.DataType;
import com.proptechos.model.actuation.KeyValueDefinition;
import com.proptechos.model.actuation.RestrictionType;
import com.proptechos.model.actuation.ValueRestriction;
import java.util.Arrays;

public class ActuationInterfaceDataBuilder {

  private final KeyValueDefinition keyValueDefinition;

  private ActuationInterfaceDataBuilder() {
    this.keyValueDefinition = new KeyValueDefinition();
  }

  public ActuationInterfaceDataBuilder dataType(DataType dataType) {
    keyValueDefinition.setValueDatatype(dataType);
    return this;
  }

  public ActuationInterfaceDataBuilder restriction(
      RestrictionType restrictionType, String...restrictionValues) {
    return restriction(restrictionType, 0, restrictionValues);
  }

  public ActuationInterfaceDataBuilder restriction(
      RestrictionType restrictionType, int resolution, String...restrictionValues) {
    ValueRestriction restriction = new ValueRestriction();
    restriction.setValueRestrictionResolution(resolution);
    restriction.setValueRestrictionType(restrictionType);
    restriction.setValueRestrictionValues(Arrays.asList(restrictionValues));
    keyValueDefinition.setValueRestriction(restriction);
    return this;
  }

  public KeyValueDefinition build() {
    return keyValueDefinition;
  }

  public static ActuationInterfaceDataBuilder builder() {
    return new ActuationInterfaceDataBuilder();
  }

}
