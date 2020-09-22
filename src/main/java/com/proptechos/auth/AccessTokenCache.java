package com.proptechos.auth;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AccessTokenCache {

  private static volatile AccessTokenCache INSTANCE;
  private static final Object lock = new Object();

  private volatile IAuthenticationResult authResult;
  private Disposable authSubscription;

  private final Logger log = LoggerFactory.getLogger(AccessTokenCache.class);


  private AccessTokenCache() { }

  public static AccessTokenCache getInstance() {
    if(INSTANCE == null) {
      synchronized(lock) {
        if(INSTANCE == null) {
          INSTANCE = new AccessTokenCache();
        }
      }
    }

    return INSTANCE;
  }

  public void subscribeOnTokenRefresh(Observable<IAuthenticationResult> refreshObs) {
    authSubscription = refreshObs.subscribe(
        result -> {
          log.debug("New auth result has been obtained.");
          authResult = result;
        },
        throwable -> {
          log.warn("Failed to re-/subscribe on token refresh: ", throwable);
          throw new RuntimeException(throwable);
        });
  }

  public void unsubscribeFromTokenRefresh() {
    authSubscription.dispose();
  }

  public IAuthenticationResult authResult() {
    return authResult;
  }

}
