package com.proptechos.model.auth;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * KafkaRetryConfig represents configuration data used to set up Azure Kafka enabled eventhub login frequency and retry options in case of login failure.
 *
 */
public class KafkaRetryConfig {

  private final int interval;

  private final int retryNumber;

  private final TimeUnit timeUnit;

  private KafkaRetryConfig(ConfigBuilder builder) {
    this.interval = builder.interval;
    this.retryNumber = builder.retryNumber;
    this.timeUnit = builder.timeUnit;
  }

  public int getInterval() {
    return interval;
  }

  public int getRetryNumber() {
    return retryNumber;
  }

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

  /**
   * @return builder for construction KafkaRetryConfig instance
   */
  public static ConfigBuilder builder() {
    return new ConfigBuilder();
  }

  public static class ConfigBuilder {

    protected static final int DEFAULT_INTERVAL = 1;
    protected static final TimeUnit DEFAULT_INTERVAL_UNIT = TimeUnit.SECONDS;
    protected static final int DEFAULT_AUTH_RETRY_NUMBER = 5;

    private int interval;

    private int retryNumber;

    private TimeUnit timeUnit;

    private ConfigBuilder() { }

    /**
     * @param interval how frequent login should be executed
     * @return KafkaRetryConfig builder.
     *
     * @apiNote This value should be greater than 0. By default is set to  {@value ConfigBuilder#DEFAULT_INTERVAL}.
     */
    public ConfigBuilder interval(int interval) {
      this.interval = interval;
      return this;
    }

    /**
     * @param timeUnit what time units will be used to evaluate login interval
     * @return KafkaRetryConfig builder.
     *
     * @apiNote This value should be greater than 0. By default is set to  {@value ConfigBuilder#DEFAULT_INTERVAL_UNIT}.
     *
     * @see java.util.concurrent.TimeUnit
     */
    public ConfigBuilder timeUnit(TimeUnit timeUnit) {
      this.timeUnit = timeUnit;
      return this;
    }

    /**
     * @param retryNumber how frequent login should be executed
     * @return KafkaRetryConfig builder.
     *
     * @apiNote This value should be greater than 0. By default is set to  {@value ConfigBuilder#DEFAULT_INTERVAL}.
     */
    public ConfigBuilder retryNumber(int retryNumber) {
      this.retryNumber = retryNumber;
      return this;
    }

    public KafkaRetryConfig build() {
      if (this.interval <= 0) {
        this.interval = 1;
      }
      if (this.retryNumber <= 0) {
        this.retryNumber = 5;
      }
      if (Objects.isNull(this.timeUnit)) {
        this.timeUnit = TimeUnit.SECONDS;
      }
      return new KafkaRetryConfig(this);
    }
  }

}
