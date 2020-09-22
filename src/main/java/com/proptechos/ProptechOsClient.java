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
import org.apache.kafka.common.protocol.types.Field.Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    this.serviceFactory = new ServiceFactory(applicationClientBuilder.baseAppUrl);
    initCloudLogin(applicationClientBuilder.baseAppUrl, applicationClientBuilder);
  }

  private void initCloudLogin(String baseAppUrl, ApplicationClientBuilder applicationClientBuilder) {
    try {
      AuthenticationConfig config = applicationClientBuilder.config;
      ConfidentialClientApplication app = ConfidentialClientApplication
          .builder(
              config.getClientId(),
              ClientCredentialFactory.createFromSecret(config.getClientSecret()))
          .authority(config.getAuthority())
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
      return ProptechOsClient.getInstance(this);
    }

  }

}
