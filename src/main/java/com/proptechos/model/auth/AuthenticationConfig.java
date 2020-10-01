package com.proptechos.model.auth;

/**
 * AuthenticationConfig represents configuration data used to authorize in Azure Active Directory
 *
 * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-client-creds-grant-flow">
 *    *   Azure AD: Client Credentials Flow</a>
 */
public class AuthenticationConfig {

  private final String clientId;

  private final String clientSecret;

  private AuthenticationConfig(ConfigBuilder builder) {
    this.clientId = builder.clientId;
    this.clientSecret = builder.clientSecret;
  }

  /**
   * @return Azure application client id
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * @return Azure application client secret
   */
  public String getClientSecret() {
    return clientSecret;
  }

  /**
   * @return AuthenticationConfig builder
   *
   */
  public static ConfigBuilder builder() {
    return new ConfigBuilder();
  }

  public static class ConfigBuilder {

    private String clientId;

    private String clientSecret;

    private ConfigBuilder() {}

    /**
     * @param clientId Azure application client id
     * @return builder
     */
    public ConfigBuilder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    /**
     * @param clientSecret Azure application client secret
     * @return builder
     */
    public ConfigBuilder clientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
      return this;
    }

    /**
     * @return AuthenticationConfig instance
     */
    public AuthenticationConfig build() {
      return new AuthenticationConfig(this);
    }

  }

}
