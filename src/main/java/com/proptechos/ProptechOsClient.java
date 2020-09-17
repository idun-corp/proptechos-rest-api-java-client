package com.proptechos;

import static com.proptechos.utils.ClientUtils.normalizeUrl;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.proptechos.auth.AccessTokenCache;
import com.proptechos.auth.ClientCredentialService;
import com.proptechos.exception.ProptechOsServiceException;
import com.proptechos.model.AuthRetryConfig;
import com.proptechos.model.AuthenticationConfig;
import com.proptechos.service.ServiceFactory;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProptechOsClient {

  private final Logger log = LoggerFactory.getLogger(ProptechOsClient.class);
  private final String baseAppUrl;

  public ServiceFactory serviceFactory() {
    return new ServiceFactory(baseAppUrl);
  }

  private ProptechOsClient(ApplicationClientBuilder applicationClientBuilder) {
    this.baseAppUrl = applicationClientBuilder.baseAppUrl;
    initCloudLogin(applicationClientBuilder);
  }

  private void initCloudLogin(ApplicationClientBuilder applicationClientBuilder) {
    try {
      AuthenticationConfig config = applicationClientBuilder.config;
      ConfidentialClientApplication app = ConfidentialClientApplication
          .builder(
              config.getClientId(),
              ClientCredentialFactory.createFromSecret(config.getClientSecret()))
          .authority(config.getAuthority())
          .build();

      Set<String> scopes = new HashSet<>(
          Collections.singletonList(this.baseAppUrl + "/.default"));
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

  public static ApplicationClientBuilder applicationClientBuilder(
      String baseAppUrl, AuthenticationConfig config) {
    return new ApplicationClientBuilder(baseAppUrl, config);
  }

  public static class ApplicationClientBuilder {

    private final String baseAppUrl;
    private final AuthenticationConfig config;
    private AuthRetryConfig authRetryConfig;

    private ApplicationClientBuilder(
        String baseAppUrl, AuthenticationConfig config) {
      this.baseAppUrl = normalizeUrl(baseAppUrl);
      this.config = config;
    }

    public ApplicationClientBuilder authRetryConfig(AuthRetryConfig authRetryConfig) {
      this.authRetryConfig = authRetryConfig;
      return this;
    }

    public ProptechOsClient build() {
      if (Objects.isNull(this.authRetryConfig)) {
        this.authRetryConfig = AuthRetryConfig.builder().build();
      }
      return new ProptechOsClient(this);
    }

  }

}
