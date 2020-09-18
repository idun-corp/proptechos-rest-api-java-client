package com.proptechos.model.auth;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthRetryConfig {

  private final int retryInterval;

  private final int retryNumber;

  private final TimeUnit timeUnit;

  private AuthRetryConfig(ConfigBuilder builder) {
    this.retryInterval = builder.retryInterval;
    this.retryNumber = builder.retryNumber;
    this.timeUnit = builder.timeUnit;
  }

  public int getRetryInterval() {
    return retryInterval;
  }

  public int getRetryNumber() {
    return retryNumber;
  }

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

  public static ConfigBuilder builder() {
    return new ConfigBuilder();
  }

  public static class ConfigBuilder {

    private int retryInterval;

    private int retryNumber;

    private TimeUnit timeUnit;

    private ConfigBuilder() { }

    public ConfigBuilder retryInterval(int retryInterval) {
      this.retryInterval = retryInterval;
      return this;
    }

    public ConfigBuilder retryNumber(int retryNumber) {
      this.retryNumber = retryNumber;
      return this;
    }

    public ConfigBuilder timeUnit(TimeUnit timeUnit) {
      this.timeUnit = timeUnit;
      return this;
    }

    public AuthRetryConfig build() {
      if (this.retryInterval <= 0) {
        this.retryInterval = 55;
      }
      if (this.retryNumber <= 0) {
        this.retryNumber = 5;
      }
      if (Objects.isNull(this.timeUnit)) {
        this.timeUnit = TimeUnit.MINUTES;
      }
      return new AuthRetryConfig(this);
    }
  }

}
