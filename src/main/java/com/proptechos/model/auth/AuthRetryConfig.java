package com.proptechos.model.auth;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * AuthRetryConfig represents configuration data used to set up Azure AD login frequency and retry options in case of login failure.
 *
 */
public class AuthRetryConfig {

  private final int interval;

  private final int retryNumber;

  private final TimeUnit timeUnit;

  private AuthRetryConfig(ConfigBuilder builder) {
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
   * @return builder for construction AuthRetryConfig instance
   */
  public static ConfigBuilder builder() {
    return new ConfigBuilder();
  }

  public static class ConfigBuilder {

    protected static final int DEFAULT_INTERVAL = 55;
    protected static final TimeUnit DEFAULT_INTERVAL_UNIT = TimeUnit.MINUTES;
    protected static final int DEFAULT_AUTH_RETRY_NUMBER = 5;

    private int interval;

    private int retryNumber;

    private TimeUnit timeUnit;

    private ConfigBuilder() { }

    /**
     * @param interval how frequent login should be executed
     * @return AuthRetryConfig builder.
     *
     * @apiNote This value should be greater than 0. By default is set to  {@value ConfigBuilder#DEFAULT_INTERVAL}.
     */
    public ConfigBuilder interval(int interval) {
      this.interval = interval;
      return this;
    }

    /**
     * @param timeUnit what time units will be used to evaluate login interval
     * @return AuthRetryConfig builder.
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
     * @param retryNumber how many times auth will be retried in case of any error
     * @return AuthRetryConfig builder.
     *
     * @apiNote This value should be greater than 0. By default is set to  {@value ConfigBuilder#DEFAULT_AUTH_RETRY_NUMBER}.
     */
    public ConfigBuilder retryNumber(int retryNumber) {
      this.retryNumber = retryNumber;
      return this;
    }

    /**
     * @return AuthRetryConfig instance
     */
    public AuthRetryConfig build() {
      if (this.interval <= 0) {
        this.interval = DEFAULT_INTERVAL;
      }
      if (this.retryNumber <= 0) {
        this.retryNumber = DEFAULT_AUTH_RETRY_NUMBER;
      }
      if (Objects.isNull(this.timeUnit)) {
        this.timeUnit = DEFAULT_INTERVAL_UNIT;
      }
      return new AuthRetryConfig(this);
    }
  }

}
