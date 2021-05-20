package com.proptechos.auth;

import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.proptechos.exception.ServiceAccessDeniedException;
import com.proptechos.model.auth.AuthRetryConfig;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientCredentialService {

    private final ConfidentialClientApplication app;
    private final ClientCredentialParameters params;
    private final AuthRetryConfig authRetryConfig;

    private final Logger log = LoggerFactory.getLogger(ClientCredentialService.class);

    public ClientCredentialService(
        ConfidentialClientApplication app,
        ClientCredentialParameters params,
        AuthRetryConfig authRetryConfig) {
        this.app = app;
        this.params = params;
        this.authRetryConfig = authRetryConfig;
    }

    public Observable<IAuthenticationResult> getAuthResult() {
        return Observable.merge(login(), Observable.interval(5,
            authRetryConfig.getInterval(), authRetryConfig.getTimeUnit())
            .flatMap(i -> login())
            .retryWhen(errors -> {
                AtomicInteger count = new AtomicInteger();
                return errors
                    .takeWhile(e -> count.getAndIncrement() != authRetryConfig.getRetryNumber())
                    .flatMap(e -> {
                        log.debug("Delay retry by " + count.get() + " second(s)");
                        return Observable.timer(count.get(), TimeUnit.SECONDS);
                    });
            }));
    }

    private Observable<IAuthenticationResult> login() {
        try {
            return Observable.fromFuture(app.acquireToken(params));
        } catch (Exception e) {
            return Observable.error(
                new ServiceAccessDeniedException("Cloud Active Directory authentication failed", e));
        }
    }

}
