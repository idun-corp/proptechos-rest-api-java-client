package com.proptechos.model.auth;

import java.util.Objects;

/**
 * AuthenticationConfig represents configuration data used to authorize in Azure Active Directory
 *
 * @see <a href="https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-client-creds-grant-flow">
 * *   Azure AD: Client Credentials Flow</a>
 */
public class AuthenticationConfig {

    private final String clientId;

    private final String clientSecret;

    private final String scope;

    private AuthenticationConfig(ConfigBuilder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.scope = builder.scope;
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
     * @return Application scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * @return AuthenticationConfig builder
     */
    public static ConfigBuilder builder() {
        return new ConfigBuilder();
    }

    public static class ConfigBuilder {

        private static final String DEFAULT_SCOPE = "https://proptechos.onmicrosoft.com/multi/api/.default";

        private String clientId;

        private String clientSecret;

        private String scope;

        private ConfigBuilder() {
        }

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
         * @param scope Application scope
         * @return builder
         */
        public ConfigBuilder scope(String scope) {
            this.scope = scope;
            return this;
        }

        /**
         * @return AuthenticationConfig instance
         */
        public AuthenticationConfig build() {
            if (Objects.isNull(scope)) {
                scope = DEFAULT_SCOPE;
            }
            return new AuthenticationConfig(this);
        }

    }

}
