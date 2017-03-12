package org.openstack.android.summit.dagger.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.api.OAuth2AccessTokenAuthenticator;
import org.openstack.android.summit.common.api.OAuth2AccessTokenInterceptor;
import org.openstack.android.summit.common.api.OAuth2AccessTokenPostSendStrategy;
import org.openstack.android.summit.common.api.SummitSelector;
import org.openstack.android.summit.common.security.oidc.IOIDCConfigurationManager;
import org.openstack.android.summit.common.security.ITokenManagerFactory;
import org.openstack.android.summit.common.security.TokenManagerFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by smarcet on 11/8/16.
 */
@Module
public class RestApiModule {

    @Provides
    @Named("MemberProfile")
    @Singleton
    public Authenticator providesAuthenticator(ITokenManagerFactory tokenManagerFactory) {
        return new OAuth2AccessTokenAuthenticator
        (
            tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.OIDC),
            new OAuth2AccessTokenPostSendStrategy()
        );
    }

    @Provides
    @Named("MemberProfile")
    @Singleton
    public Interceptor providesInterceptor(ITokenManagerFactory tokenManagerFactory) {
        return new OAuth2AccessTokenInterceptor
        (
            tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.OIDC),
            new OAuth2AccessTokenPostSendStrategy()
        );
    }

    @Provides
    @Named("ServiceProfile")
    @Singleton
    public Authenticator providesAuthenticatorService(ITokenManagerFactory tokenManagerFactory) {
        return new OAuth2AccessTokenAuthenticator
        (
            tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.ServiceAccount),
            new OAuth2AccessTokenPostSendStrategy()
        );
    }

    @Provides
    @Named("ServiceProfile")
    @Singleton
    public Interceptor providesInterceptorService(ITokenManagerFactory tokenManagerFactory) {
        return new OAuth2AccessTokenInterceptor
        (
            tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.ServiceAccount),
            new OAuth2AccessTokenPostSendStrategy()
        );
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public ISummitSelector providesSummitSelector(ISession session){
        return new SummitSelector(session);
    }

    @Provides
    @Named("MemberProfile")
    @Singleton
    public OkHttpClient providesOkHttpClient
    (
        @Named("MemberProfile") Authenticator authenticator,
        @Named("MemberProfile") Interceptor interceptor
    )
    {
        return buildHttpClient(authenticator, interceptor);
    }

    @Provides
    @Named("ServiceProfile")
    @Singleton
    public OkHttpClient providesOkHttpClientService
    (
        @Named("ServiceProfile") Authenticator authenticator,
        @Named("ServiceProfile") Interceptor interceptor
    )
    {
        return buildHttpClient(authenticator, interceptor);
    }

    private OkHttpClient buildHttpClient
    (
        Authenticator authenticator,
        Interceptor interceptor
    )
    {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // configuration
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(true);

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        httpClient.addInterceptor(interceptor);
        // refresh access token (401)
        httpClient.authenticator(authenticator);
        return httpClient.build();
    }

    @Provides
    @Named("MemberProfile")
    @Singleton
    public Retrofit providesRetrofit
    (
        IOIDCConfigurationManager configurationManager,
        Gson gson,
        @Named("MemberProfile") OkHttpClient okHttpClient
    )
    {
        return buildRetrofitClient(configurationManager, gson, okHttpClient);
    }

    @Provides
    @Named("MemberProfileRXJava2")
    @Singleton
    public Retrofit providesRetrofitRXJava2
            (
                    IOIDCConfigurationManager configurationManager,
                    Gson gson,
                    @Named("MemberProfile") OkHttpClient okHttpClient
            )
    {
        return buildRetrofitClientRXJava2(configurationManager, gson, okHttpClient);
    }

    @Provides
    @Named("ServiceProfile")
    @Singleton
    public Retrofit providesRetrofitService
    (
        IOIDCConfigurationManager configurationManager,
        Gson gson,
        @Named("ServiceProfile") OkHttpClient okHttpClient
    )
    {
        return buildRetrofitClient(configurationManager, gson, okHttpClient);
    }

    @Provides
    @Named("ServiceProfileRXJava2")
    @Singleton
    public Retrofit providesRetrofitServiceRXJava2
            (
                    IOIDCConfigurationManager configurationManager,
                    Gson gson,
                    @Named("ServiceProfile") OkHttpClient okHttpClient
            )
    {
        return buildRetrofitClientRXJava2(configurationManager, gson, okHttpClient);
    }

    private Retrofit buildRetrofitClient
    (
        IOIDCConfigurationManager configurationManager,
        Gson gson,
        OkHttpClient okHttpClient
    )
    {
        return new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(configurationManager.getResourceServerBaseUrl() + "/api/")
            .client(okHttpClient)
            .build();
    }

    private Retrofit buildRetrofitClientRXJava2
            (
                    IOIDCConfigurationManager configurationManager,
                    Gson gson,
                    OkHttpClient okHttpClient
            )
    {
        return new Retrofit.Builder()
                .baseUrl(configurationManager.getResourceServerBaseUrl() + "/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }
}
