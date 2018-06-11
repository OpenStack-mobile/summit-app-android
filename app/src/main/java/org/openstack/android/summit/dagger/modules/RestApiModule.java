package org.openstack.android.summit.dagger.modules;

import android.os.Build;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.api.OAuth2AccessTokenAuthenticator;
import org.openstack.android.summit.common.api.OAuth2AccessTokenInterceptor;
import org.openstack.android.summit.common.api.OAuth2AccessTokenPostSendStrategy;
import org.openstack.android.summit.common.api.SummitSelector;
import org.openstack.android.summit.common.api.Tls12SocketFactory;
import org.openstack.android.summit.common.security.oidc.IOIDCConfigurationManager;
import org.openstack.android.summit.common.security.ITokenManagerFactory;
import org.openstack.android.summit.common.security.TokenManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by smarcet on 11/8/16.
 */
@Module
public class RestApiModule {

    private static final int       ApiTimeout        = 60 ; //secs;
    private static final boolean   RetryOnConnection = true;
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

    private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    private OkHttpClient buildHttpClient
    (
        Authenticator authenticator,
        Interceptor interceptor
    )
    {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // configuration
        httpClient.readTimeout(ApiTimeout, TimeUnit.SECONDS);
        httpClient.connectTimeout(ApiTimeout, TimeUnit.SECONDS);
        httpClient.writeTimeout(ApiTimeout, TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(RetryOnConnection);

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        httpClient.addInterceptor(interceptor);
        // refresh access token (401)
        httpClient.authenticator(authenticator);
        return enableTls12OnPreLollipop(httpClient).build();
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
