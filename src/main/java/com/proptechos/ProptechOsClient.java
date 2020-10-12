package com.proptechos;

import static com.proptechos.utils.ClientUtils.normalizeUrl;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.proptechos.auth.AccessTokenCache;
import com.proptechos.auth.ClientCredentialService;
import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.auth.AuthRetryConfig;
import com.proptechos.model.auth.AuthenticationConfig;
import com.proptechos.service.ServiceFactory;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry singleton class to integrate with ProptechOS API
 */
public class ProptechOsClient {

  private final Logger log = LoggerFactory.getLogger(ProptechOsClient.class);

  private static volatile ProptechOsClient INSTANCE;
  private static final Object lock = new Object();

  private final ServiceFactory serviceFactory;

  public ServiceFactory serviceFactory() {
    return serviceFactory;
  }

  private static ProptechOsClient getInstance(ApplicationClientBuilder applicationClientBuilder) {
    if(INSTANCE == null) {
      synchronized(lock) {
        if(INSTANCE == null) {
          INSTANCE = new ProptechOsClient(applicationClientBuilder);
        }
      }
    }

    return INSTANCE;
  }

  private ProptechOsClient(ApplicationClientBuilder applicationClientBuilder) {
    this.serviceFactory = new ServiceFactory(
        applicationClientBuilder.baseAppUrl,
        applicationClientBuilder.config.getClientId());
    initCloudLogin(applicationClientBuilder.baseAppUrl, applicationClientBuilder);
  }

  private void initCloudLogin(String baseAppUrl, ApplicationClientBuilder applicationClientBuilder) {
    try {
      AuthenticationConfig config = applicationClientBuilder.config;
      ConfidentialClientApplication app = ConfidentialClientApplication
          .builder(
              config.getClientId(),
              ClientCredentialFactory.createFromSecret(config.getClientSecret()))
          .authority("https://login.microsoftonline.com/d4218456-670f-42ad-9f6a-885ae15b6645/")
          .build();

      Set<String> scopes = new HashSet<>(
          Collections.singletonList(baseAppUrl + "/.default"));
      ClientCredentialParameters params =
          ClientCredentialParameters.builder(scopes).build();

      ClientCredentialService service =
          new ClientCredentialService(app, params, applicationClientBuilder.authRetryConfig);
      AccessTokenCache tokenCache = AccessTokenCache.getInstance();

      tokenCache.subscribeOnTokenRefresh(service.getAuthResult());
    } catch (MalformedURLException e) {
      throw new ProptechOsServiceException("Failed to construct cloud Active Directory client: ", e);
    }
  }

  /**
   * @param baseAppUrl base ProptechOS API url
   * @return ApplicationClientBuilder - builder for constructing ProptechOsClient class instance
   *
   */
  public static ApplicationClientBuilder applicationClientBuilder(String baseAppUrl) {
    return new ApplicationClientBuilder(baseAppUrl);
  }

  public static class ApplicationClientBuilder {

    private final String baseAppUrl;
    private AuthenticationConfig config;
    private AuthRetryConfig authRetryConfig;

    private ApplicationClientBuilder(String baseAppUrl) {
      this.baseAppUrl = normalizeUrl(baseAppUrl);
    }

    /**
     * @param authConfig configuration data needed to auth Azure AD
     * @return ApplicationClientBuilder
     *
     * @see com.proptechos.model.auth.AuthenticationConfig
     */
    public ApplicationClientBuilder authConfig(AuthenticationConfig authConfig) {
      this.config = authConfig;
      return this;
    }

    /**
     * @param authRetryConfig optional configuration data to set up retry auth options
     * @return ApplicationClientBuilder
     *
     * @see com.proptechos.model.auth.AuthRetryConfig
     */
    public ApplicationClientBuilder authRetryConfig(AuthRetryConfig authRetryConfig) {
      this.authRetryConfig = authRetryConfig;
      return this;
    }

    /**
     * @return ProptechOsClient class instance
     */
    public ProptechOsClient build() {
      if (Objects.isNull(this.authRetryConfig)) {
        this.authRetryConfig = AuthRetryConfig.builder().build();
      }
      return ProptechOsClient.getInstance(this);
    }

  }

}
