package com.proptechos.model;

public class AuthenticationConfig {

  private final String clientId;

  private final String clientSecret;

  private final String authority;

  private String applicationScope;

  private AuthenticationConfig(ConfigBuilder builder) {
    this.clientId = builder.clientId;
    this.clientSecret = builder.clientSecret;
    this.authority = builder.authority;
    this.applicationScope = builder.applicationScope;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public String getAuthority() {
    return authority;
  }

  public String getApplicationScope() {
    return applicationScope;
  }

  public static ConfigBuilder builder(
      String clientId,
      String clientSecret,
      String authority) {
    return new ConfigBuilder(clientId, clientSecret, authority);
  }

  public static class ConfigBuilder {

    private final String clientId;

    private final String clientSecret;

    private final String authority;

    private String applicationScope;

    private ConfigBuilder(
        String clientId,
        String clientSecret,
        String authority) {
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      this.authority = authority;
    }

    public ConfigBuilder applicationScope(String scope) {
      this.applicationScope = scope;
      return this;
    }

    public AuthenticationConfig build() {
      return new AuthenticationConfig(this);
    }

  }

}
