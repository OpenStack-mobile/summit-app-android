package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.DataUpdateDataStore;
import org.openstack.android.summit.common.data_access.GenericDataStore;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.IMemberDataStore;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeRemoteDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventRemoteDataStore;
import org.openstack.android.summit.common.data_access.ISummitRemoteDataStore;
import org.openstack.android.summit.common.data_access.ITrackGroupDataStore;
import org.openstack.android.summit.common.data_access.IVenueDataStore;
import org.openstack.android.summit.common.data_access.MemberDataStore;
import org.openstack.android.summit.common.data_access.MemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.PushNotificationDataStore;
import org.openstack.android.summit.common.data_access.PresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.SummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.SummitAttendeeRemoteDataStore;
import org.openstack.android.summit.common.data_access.SummitDataStore;
import org.openstack.android.summit.common.data_access.SummitEventDataStore;
import org.openstack.android.summit.common.data_access.SummitEventRemoteDataStore;
import org.openstack.android.summit.common.data_access.SummitRemoteDataStore;
import org.openstack.android.summit.common.data_access.TrackGroupDataStore;
import org.openstack.android.summit.common.data_access.VenueDataStore;
import org.openstack.android.summit.common.data_access.data_polling.ClassResolver;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdatePoller;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateProcessor;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateStrategyFactory;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdateProcessor;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdateStrategyFactory;
import org.openstack.android.summit.common.data_access.data_polling.MyScheduleDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.PresentationMaterialDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.SummitDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.SummitVenueImageDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.TrackGroupDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.VenueLocationsDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.deserialization.Deserializer;
import org.openstack.android.summit.common.data_access.deserialization.FeedbackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.GenericDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IFeedbackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IGenericDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IMemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.INonConfirmedSummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPersonDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationLinkDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationSlideDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationSpeakerDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationVideoDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPushNotificationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ITrackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ITrackGroupDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueFloorDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueRoomDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.MemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.NonConfirmedSummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PersonDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationLinkDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationSlideDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationSpeakerDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationVideoDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PushNotificationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.TrackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.TrackGroupDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueFloorDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueRoomDeserializer;
import org.openstack.android.summit.common.network.IHttpFactory;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.network.Reachability;
import org.openstack.android.summit.common.security.IOIDCConfigurationManager;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.security.ITokenManagerFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Module
public class DataAccessModule {


    @Provides
    IPersonDeserializer providesPersonDeserializer() {
        return new PersonDeserializer();
    }

    @Provides
    ISummitEventDeserializer providesSummitEventDeserializer(IGenericDeserializer genericDeserializer, IPresentationDeserializer presentationDeserializer) {
        return new SummitEventDeserializer(genericDeserializer, presentationDeserializer);
    }

    @Provides
    IVenueRoomDeserializer providesVenueRoomDeserializer(IVenueFloorDeserializer venueFloorDeserializer) {
        return new VenueRoomDeserializer(venueFloorDeserializer);
    }

    @Provides
    IVenueDeserializer providesVenueDeserializer(IGenericDeserializer genericDeserializer, IVenueFloorDeserializer venueFloorDeserializer) {
        return new VenueDeserializer(genericDeserializer, venueFloorDeserializer);
    }

    @Provides
    ISummitDeserializer providesSummitDeserializer(IGenericDeserializer genericDeserializer,
                                                   IVenueDeserializer venueDeserializer,
                                                   IVenueRoomDeserializer venueRoomDeserializer,
                                                   ISummitEventDeserializer summitEventDeserializer,
                                                   IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
                                                   ITrackGroupDeserializer trackGroupDeserializer,
                                                   ITrackDeserializer trackDeserializer)
    {
        return new SummitDeserializer(genericDeserializer, venueDeserializer, venueRoomDeserializer, summitEventDeserializer, presentationSpeakerDeserializer, trackGroupDeserializer, trackDeserializer);
    }

    @Provides
    IPresentationLinkDeserializer providesPresentationLinkDeserializer() {
        return new PresentationLinkDeserializer();
    }

    @Provides
    IPresentationVideoDeserializer providesPresentationVideoDeserializer() {
        return new PresentationVideoDeserializer();
    }

    @Provides
    IPresentationSlideDeserializer providesPresentationSlideDeserializer() {
        return new PresentationSlideDeserializer();
    }

    @Provides
    IPresentationDeserializer providesPresentationDeserializer
    (
        IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
        IPresentationLinkDeserializer presentationLinkDeserializer,
        IPresentationSlideDeserializer presentationSlideDeserializer,
        IPresentationVideoDeserializer presentationVideoDeserializer
    )
    {
        return new PresentationDeserializer(presentationSpeakerDeserializer, presentationLinkDeserializer, presentationVideoDeserializer, presentationSlideDeserializer);
    }

    @Provides
    ISummitAttendeeDeserializer providesSummitAttendeeDeserializer() {
        return new SummitAttendeeDeserializer();
    }

    @Provides
    IPresentationSpeakerDeserializer providesPresentationSpeakerDeserializer(IPersonDeserializer personDeserializer) {
        return new PresentationSpeakerDeserializer(personDeserializer);
    }


    @Provides
    IPushNotificationDeserializer providesPushNotificationDeserializer() {
        return new PushNotificationDeserializer();
    }

    @Provides
    IVenueFloorDeserializer providesVenueFloorDeserializer() {
        return new VenueFloorDeserializer();
    }

    @Provides
    ITrackDeserializer providesTrackDeserializerDeserializer() {
        return new TrackDeserializer();
    }

    @Provides
    IFeedbackDeserializer providesFeedbackDeserializer() {
        return new FeedbackDeserializer();
    }

    @Provides
    IGenericDeserializer providesGenericDeserializer() {
        return new GenericDeserializer();
    }

    @Provides
    IMemberDeserializer providesMemberDeserializer
    (
        IPersonDeserializer personDeserializer,
        IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
        ISummitAttendeeDeserializer summitAttendeeDeserializer,
        IFeedbackDeserializer feedbackDeserializer
    )
    {
        return new MemberDeserializer(personDeserializer, presentationSpeakerDeserializer, summitAttendeeDeserializer, feedbackDeserializer);
    }

    @Provides
    ITrackGroupDeserializer providesTrackGroupDeserializer(ITrackDeserializer trackDeserializer) {
        return new TrackGroupDeserializer(trackDeserializer);
    }

    @Provides
    INonConfirmedSummitAttendeeDeserializer providesNonConfirmedSummitAttendeeDeserializer() {
        return new NonConfirmedSummitAttendeeDeserializer();
    }

    @Provides
    IDeserializer providesDeserializer(IGenericDeserializer genericDeserializer,
                                       IFeedbackDeserializer feedbackDeserializer,
                                       IMemberDeserializer memberDeserializer,
                                       IPresentationDeserializer presentationDeserializer,
                                       IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
                                       ISummitAttendeeDeserializer summitAttendeeDeserializer,
                                       ISummitDeserializer summitDeserializer,
                                       ISummitEventDeserializer summitEventDeserializer,
                                       ITrackGroupDeserializer trackGroupDeserializer,
                                       ITrackDeserializer trackDeserializer,
                                       IVenueRoomDeserializer venueRoomDeserializer,
                                       IVenueDeserializer venueDeserializer,
                                       IVenueFloorDeserializer venueFloorDeserializer,
                                       IPushNotificationDeserializer pushNotificationDeserializer
                                       )
    {
        return new Deserializer(genericDeserializer,
                feedbackDeserializer,
                memberDeserializer,
                presentationDeserializer,
                presentationSpeakerDeserializer,
                summitAttendeeDeserializer,
                summitDeserializer,
                summitEventDeserializer,
                trackGroupDeserializer,
                trackDeserializer,
                venueRoomDeserializer,
                venueDeserializer,
                venueFloorDeserializer,
                pushNotificationDeserializer
                );
    }

    @Provides
    ISummitRemoteDataStore providesSummitRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer, IOIDCConfigurationManager ioidcConfigurationManager) {
        SummitRemoteDataStore store = new SummitRemoteDataStore(httpTaskFactory, deserializer);
        store.setBaseResourceServerUrl(ioidcConfigurationManager.getResourceServerBaseUrl());
        return store;
    }

    @Provides
    ISummitDataStore providesSummitDataStore(ISummitRemoteDataStore summitRemoteDataStore) {
        return new SummitDataStore(summitRemoteDataStore);
    }

    @Provides
    IMemberRemoteDataStore providesMemberRemoteDataStore(INonConfirmedSummitAttendeeDeserializer nonConfirmedSummitAttendeeDeserializer,  IHttpTaskFactory httpTaskFactory, IDeserializer deserializer, IOIDCConfigurationManager ioidcConfigurationManager) {
        MemberRemoteDataStore store = new MemberRemoteDataStore(nonConfirmedSummitAttendeeDeserializer, httpTaskFactory, deserializer);
        store.setBaseResourceServerUrl(ioidcConfigurationManager.getResourceServerBaseUrl());
        store.setUserInfoEndpointUrl(ioidcConfigurationManager.buildIdentityProviderUrls().getUserInfoEndpoint());
        return store;
    }

    @Provides
    IGenericDataStore providesGenericDataStore() {
        return new GenericDataStore();
    }

    @Provides
    ITrackGroupDataStore providesTrackGroupDataStore() {
        return new TrackGroupDataStore();
    }

    @Provides
    IMemberDataStore providesMemberDataStore(IMemberRemoteDataStore memberRemoteDataStore) {
        return new MemberDataStore(memberRemoteDataStore);
    }

    @Provides
    ISummitEventDataStore providesSummitEventDataStore(ISummitEventRemoteDataStore summitEventRemoteDataStore) {
        return new SummitEventDataStore(summitEventRemoteDataStore);
    }

    @Provides
    ISummitEventRemoteDataStore providesSummitEventRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer, IOIDCConfigurationManager ioidcConfigurationManager) {
        SummitEventRemoteDataStore store = new SummitEventRemoteDataStore(httpTaskFactory, deserializer);
        store.setBaseResourceServerUrl(ioidcConfigurationManager.getResourceServerBaseUrl());
        return store;
    }

    @Provides
    ISummitAttendeeRemoteDataStore providesSummitAttendeeRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer, IOIDCConfigurationManager ioidcConfigurationManager) {
        SummitAttendeeRemoteDataStore store = new SummitAttendeeRemoteDataStore(httpTaskFactory, deserializer);
        store.setBaseResourceServerUrl(ioidcConfigurationManager.getResourceServerBaseUrl());
        return store;
    }

    @Provides
    ISummitAttendeeDataStore providesSummitAttendeeDataStore(ISummitAttendeeRemoteDataStore summitAttendeeRemoteDataStore) {
        return new SummitAttendeeDataStore(summitAttendeeRemoteDataStore);
    }

    @Provides
    IPresentationSpeakerDataStore providesPresentationSpeakerDataStore() {
        return new PresentationSpeakerDataStore();
    }

    @Provides
    IPushNotificationDataStore providesNotificationDataStore() {
        return new PushNotificationDataStore();
    }

    @Provides
    IDataUpdateDataStore providesDataUpdateDataStore() {
        return new DataUpdateDataStore();
    }

    @Provides
    IVenueDataStore providesVenueDataStore() {
        return new VenueDataStore();
    }

    @Provides
    IDataUpdateProcessor providesDataUpdateProcessor(IDeserializer deserializer, IDataUpdateStrategyFactory dataUpdateStrategyFactory, IDataUpdateDataStore dataUpdateDataStore) {
        return new DataUpdateProcessor(deserializer, dataUpdateStrategyFactory, dataUpdateDataStore, new ClassResolver());
    }

    @Provides
    IDataUpdateStrategyFactory providesDataUpdateStrategyFactory(IGenericDataStore genericDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, ISummitDataStore summitDataStore, ITrackGroupDataStore trackGroupDataStore, IVenueDataStore venueDataStore, ISecurityManager securityManager) {
        return new DataUpdateStrategyFactory(
                new DataUpdateStrategy(genericDataStore),
                new MyScheduleDataUpdateStrategy(genericDataStore, summitAttendeeDataStore, securityManager),
                new SummitDataUpdateStrategy(genericDataStore, summitDataStore),
                new TrackGroupDataUpdateStrategy(genericDataStore, trackGroupDataStore),
                new SummitVenueImageDataUpdateStrategy(genericDataStore, venueDataStore),
                new PresentationMaterialDataUpdateStrategy(genericDataStore),
                new VenueLocationsDataUpdateStrategy(genericDataStore)
        );
    }

    @Provides
    @Singleton
    IDataUpdatePoller providesDataUpdatePoller
    (
                    ISecurityManager securityManager,
                    ITokenManagerFactory tokenManagerFactory,
                    IDataUpdateProcessor dataUpdateProcessor,
                    IDataUpdateDataStore dataUpdateDataStore,
                    ISummitDataStore summitDataStore,
                    ISession session,
                    IOIDCConfigurationManager ioidcConfigurationManager,
                    IHttpFactory httpFactory
    ) {
        DataUpdatePoller poller = new DataUpdatePoller(
                securityManager,
                tokenManagerFactory,
                dataUpdateProcessor,
                dataUpdateDataStore,
                summitDataStore,
                session,
                httpFactory
        );
        poller.setBaseResourceServerUrl(ioidcConfigurationManager.getResourceServerBaseUrl());
        return poller;
    }

    @Provides
    @Singleton
    IReachability providesReachability() {
        return new Reachability();
    }
}