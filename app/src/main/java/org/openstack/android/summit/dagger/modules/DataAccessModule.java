package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventRemoteDataStore;
import org.openstack.android.summit.common.data_access.MemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.SummitEventRemoteDataStore;
import org.openstack.android.summit.common.data_access.data_polling.ClassResolver;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdatePoller;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateProcessor;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateStrategyFactory;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdateProcessor;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdateStrategyFactory;
import org.openstack.android.summit.common.data_access.data_polling.MyFavoriteDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.MyScheduleDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.PresentationMaterialDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.SummitDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.SummitGroupEventDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.SummitVenueImageDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.TrackGroupDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.VenueLocationsDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.WifiConnectionDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.deserialization.Deserializer;
import org.openstack.android.summit.common.data_access.deserialization.FeedbackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.GenericDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IFeedbackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IGenericDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IGroupEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IMemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.INonConfirmedSummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPersonDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationLinkDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationSlideDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISpeakerDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationVideoDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitEventWithFileDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ITrackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ITrackGroupDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueFloorDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueRoomDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IWifiConnectionDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.MemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.NonConfirmedSummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PersonDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationLinkDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationSlideDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SpeakerDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationVideoDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitEventWithFileDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitGroupEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.TrackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.TrackGroupDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueFloorDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueRoomDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.WifiConnectionDeserializer;
import org.openstack.android.summit.common.data_access.repositories.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.repositories.IEventPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.IEventTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.IImageDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyFavoriteProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyFeedbackProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyRSVPProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyScheduleProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IPresentationDataStore;
import org.openstack.android.summit.common.data_access.repositories.IPresentationLinkDataStore;
import org.openstack.android.summit.common.data_access.repositories.IPresentationSlideDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.IPresentationVideoDataStore;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITagDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITeamDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITeamPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITrackDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITrackGroupDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueFloorDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueRoomDataStore;
import org.openstack.android.summit.common.data_access.repositories.IWifiConnectionDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.DataUpdateDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.EventPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.EventTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.ImageDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.MemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.MyFavoriteProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.MyFeedbackProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.MyRSVPProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.MyScheduleProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.PresentationDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.PresentationLinkDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.PresentationSlideDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.SpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.PresentationVideoDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.PushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.SummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.SummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.SummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.SummitTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.TagDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.TeamDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.TeamPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.TrackDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.TrackGroupDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.VenueDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.VenueFloorDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.VenueRoomDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.WifiConnectionDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.DeleteRealmStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.SaveOrUpdateRealmStrategy;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.network.Reachability;
import org.openstack.android.summit.common.security.ISecurityManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Module
public class DataAccessModule {


    @Provides
    ISaveOrUpdateStrategy providesSaveOrUpdateStrategy(){ return new SaveOrUpdateRealmStrategy(); }

    @Provides
    IDeleteStrategy providesDeleteStrategy(){ return new DeleteRealmStrategy(); }

    @Provides
    IPersonDeserializer providesPersonDeserializer() {
        return new PersonDeserializer();
    }

    @Provides
    IGroupEventDeserializer providesGroupEventDeserializer() {
        return new SummitGroupEventDeserializer();
    }

    @Provides
    ISummitEventDeserializer providesSummitEventDeserializer
    (
        IGenericDeserializer genericDeserializer,
        IPresentationDeserializer presentationDeserializer,
        IGroupEventDeserializer groupEventDeserializer,
        ISummitEventWithFileDeserializer eventWithFileDeserializer
    )
    {
        return new SummitEventDeserializer(genericDeserializer, presentationDeserializer, groupEventDeserializer, eventWithFileDeserializer);
    }

    @Provides
    IVenueRoomDeserializer providesVenueRoomDeserializer(IVenueFloorDeserializer venueFloorDeserializer) {
        return new VenueRoomDeserializer(venueFloorDeserializer);
    }

    @Provides
    IVenueDeserializer providesVenueDeserializer
    (
            IGenericDeserializer genericDeserializer,
            IVenueFloorDeserializer venueFloorDeserializer,
            IVenueRoomDeserializer venueRoomDeserializer
    )
    {
        return new VenueDeserializer(genericDeserializer, venueFloorDeserializer, venueRoomDeserializer);
    }

    @Provides
    ISummitDeserializer providesSummitDeserializer(IGenericDeserializer genericDeserializer,
                                                   IVenueDeserializer venueDeserializer,
                                                   IVenueRoomDeserializer venueRoomDeserializer,
                                                   ISummitEventDeserializer summitEventDeserializer,
                                                   ISpeakerDeserializer presentationSpeakerDeserializer,
                                                   ITrackGroupDeserializer trackGroupDeserializer,
                                                   ITrackDeserializer trackDeserializer,
                                                   IWifiConnectionDeserializer wifiConnectionDeserializer)
    {
        return new SummitDeserializer(genericDeserializer, venueDeserializer, venueRoomDeserializer, summitEventDeserializer, presentationSpeakerDeserializer, trackGroupDeserializer, trackDeserializer, wifiConnectionDeserializer);
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
        ISpeakerDeserializer presentationSpeakerDeserializer,
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
    ISpeakerDeserializer providesPresentationSpeakerDeserializer(IPersonDeserializer personDeserializer) {
        return new SpeakerDeserializer(personDeserializer);
    }

    @Provides
    IVenueFloorDeserializer providesVenueFloorDeserializer() {
        return new VenueFloorDeserializer();
    }

    @Provides
    ISummitEventWithFileDeserializer providesSummitEventWithFileDeserializer(){
        return new SummitEventWithFileDeserializer();
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
        ISpeakerDeserializer presentationSpeakerDeserializer,
        ISummitAttendeeDeserializer summitAttendeeDeserializer,
        IFeedbackDeserializer feedbackDeserializer,
        ISummitEventDeserializer summitEventDeserializer
    )
    {
        return new MemberDeserializer
        (
            personDeserializer,
            presentationSpeakerDeserializer,
            summitAttendeeDeserializer,
            feedbackDeserializer,
            summitEventDeserializer
        );
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
    IWifiConnectionDeserializer providesWifiConnectionDeserializer() {
        return new WifiConnectionDeserializer();
    }

    @Provides
    IDeserializer providesDeserializer(IGenericDeserializer genericDeserializer,
                                       IFeedbackDeserializer feedbackDeserializer,
                                       IMemberDeserializer memberDeserializer,
                                       IPresentationDeserializer presentationDeserializer,
                                       ISpeakerDeserializer presentationSpeakerDeserializer,
                                       ISummitAttendeeDeserializer summitAttendeeDeserializer,
                                       ISummitDeserializer summitDeserializer,
                                       ISummitEventDeserializer summitEventDeserializer,
                                       ITrackGroupDeserializer trackGroupDeserializer,
                                       ITrackDeserializer trackDeserializer,
                                       IVenueRoomDeserializer venueRoomDeserializer,
                                       IVenueDeserializer venueDeserializer,
                                       IVenueFloorDeserializer venueFloorDeserializer,
                                       IGroupEventDeserializer groupEventDeserializer,
                                       IWifiConnectionDeserializer wifiConnectionDeserializer
                                       )
    {
        return new Deserializer
        (
            genericDeserializer,
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
            groupEventDeserializer,
            wifiConnectionDeserializer
        );
    }

    @Provides
    ISummitDataStore providesSummitDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                             IDeleteStrategy deleteStrategy) {
        return new SummitDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IMemberRemoteDataStore providesMemberRemoteDataStore
    (
                    INonConfirmedSummitAttendeeDeserializer nonConfirmedSummitAttendeeDeserializer,
                    IDeserializer deserializer,
                    @Named("MemberProfileRXJava2") Retrofit restClientRxJava,
                    ISummitSelector summitSelector
    )
    {
        return new MemberRemoteDataStore(nonConfirmedSummitAttendeeDeserializer, deserializer, restClientRxJava, summitSelector);
    }

    @Provides
    ITrackGroupDataStore providesTrackGroupDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                     IDeleteStrategy deleteStrategy) {
        return new TrackGroupDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IMemberDataStore providesMemberDataStore(IMemberRemoteDataStore memberRemoteDataStore,ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                             IDeleteStrategy deleteStrategy) {
        return new MemberDataStore(memberRemoteDataStore, saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    ISummitEventDataStore providesSummitEventDataStore
    (
        ISecurityManager securityManager,
        ISummitEventRemoteDataStore summitEventRemoteDataStore,
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        return new SummitEventDataStore(securityManager, summitEventRemoteDataStore, saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    ISummitEventRemoteDataStore providesSummitEventRemoteDataStore(IDeserializer deserializer,
                                                                   @Named("ServiceProfileRXJava2") Retrofit restClient,
                                                                   ISummitSelector summitSelector) {
        return new SummitEventRemoteDataStore(deserializer, restClient, summitSelector);
    }

    @Provides
    ISummitAttendeeDataStore providesSummitAttendeeDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                             IDeleteStrategy deleteStrategy) {
        return new SummitAttendeeDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    ISpeakerDataStore providesPresentationSpeakerDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                           IDeleteStrategy deleteStrategy) {
        return new SpeakerDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IPushNotificationDataStore providesNotificationDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                             IDeleteStrategy deleteStrategy) {
        return new PushNotificationDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IDataUpdateDataStore providesDataUpdateDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                     IDeleteStrategy deleteStrategy) {
        return new DataUpdateDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IVenueDataStore providesVenueDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                           IDeleteStrategy deleteStrategy) {
        return new VenueDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    ITrackDataStore providesTrackDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                           IDeleteStrategy deleteStrategy)
    { return new TrackDataStore(saveOrUpdateStrategy, deleteStrategy); }

    @Provides
    IImageDataStore providesImageDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                           IDeleteStrategy deleteStrategy)
    { return new ImageDataStore(saveOrUpdateStrategy, deleteStrategy); }


    @Provides
    IDataUpdateProcessor providesDataUpdateProcessor(IDeserializer deserializer, IDataUpdateStrategyFactory dataUpdateStrategyFactory, IDataUpdateDataStore dataUpdateDataStore) {
        return new DataUpdateProcessor(deserializer, dataUpdateStrategyFactory, dataUpdateDataStore, new ClassResolver());
    }


    @Provides
    IDataUpdateStrategyFactory providesDataUpdateStrategyFactory
    (
            ISaveOrUpdateStrategy saveOrUpdateStrategy,
            IDeleteStrategy deleteStrategy,
            ISummitDataStore summitDataStore,
            ITrackGroupDataStore trackGroupDataStore,
            IVenueDataStore venueDataStore,
            IVenueRoomDataStore venueRoomDataStore,
            IVenueFloorDataStore venueFloorDataStore,
            IImageDataStore imageDataStore,
            IPresentationDataStore presentationDataStore,
            IPresentationVideoDataStore presentationVideoDataStore,
            IPresentationSlideDataStore presentationSlideDataStore,
            IPresentationLinkDataStore presentationLinkDataStore,
            IMemberDataStore memberDataStore,
            IWifiConnectionDataStore wifiConnectionDataStore,
            ISecurityManager securityManager,
            ISummitSelector summitSelector
    )
    {
        return new DataUpdateStrategyFactory(
                new DataUpdateStrategy(summitSelector),
                new MyScheduleDataUpdateStrategy(memberDataStore, securityManager, summitSelector),
                new MyFavoriteDataUpdateStrategy(memberDataStore, securityManager, summitSelector),
                new SummitDataUpdateStrategy(summitDataStore, summitSelector),
                new TrackGroupDataUpdateStrategy(trackGroupDataStore, summitSelector),
                new SummitVenueImageDataUpdateStrategy(imageDataStore, venueDataStore, summitSelector),
                new PresentationMaterialDataUpdateStrategy
                (
                    presentationDataStore,
                    presentationSlideDataStore,
                    presentationVideoDataStore,
                    presentationLinkDataStore,
                    summitSelector
                ),
                new VenueLocationsDataUpdateStrategy(venueDataStore, venueFloorDataStore, venueRoomDataStore, summitSelector),
                new SummitGroupEventDataUpdateStrategy(securityManager, summitSelector),
                new WifiConnectionDataUpdateStrategy(wifiConnectionDataStore, summitSelector)
        );
    }

    @Provides
    @Singleton
    IDataUpdatePoller providesDataUpdatePoller
    (
                    ISecurityManager securityManager,
                    IDataUpdateProcessor dataUpdateProcessor,
                    IDataUpdateDataStore dataUpdateDataStore,
                    ISummitDataStore summitDataStore,
                    ISession session,
                    @Named("MemberProfile") Retrofit restClientUserProfile,
                    @Named("ServiceProfile") Retrofit restClientServiceProfile,
                    ISummitSelector summitSelector

    ) {
        return new DataUpdatePoller(
                securityManager,
                dataUpdateProcessor,
                dataUpdateDataStore,
                summitDataStore,
                session,
                restClientUserProfile,
                restClientServiceProfile,
                summitSelector
        );
    }

    @Provides
    @Singleton
    IReachability providesReachability() {
        return new Reachability();
    }

    @Provides
    ITeamDataStore providesTeamDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                         IDeleteStrategy deleteStrategy)
    {
        return new TeamDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IVenueFloorDataStore providesVenueFloorDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                     IDeleteStrategy deleteStrategy)
    {
        return new VenueFloorDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IVenueRoomDataStore providesVenueRoomDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                   IDeleteStrategy deleteStrategy)
    {
        return new VenueRoomDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    ISummitTypeDataStore providesSummitTypeDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                     IDeleteStrategy deleteStrategy)
    {
        return new SummitTypeDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IEventTypeDataStore providesEventTypeDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                                   IDeleteStrategy deleteStrategy)
    {
        return new EventTypeDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    ITagDataStore providesTagDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy,
                                       IDeleteStrategy deleteStrategy)
    {
        return new TagDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    ITeamPushNotificationDataStore providesTeamPushNotificationDataStore
    (
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        return new TeamPushNotificationDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IEventPushNotificationDataStore providesEventPushNotificationDataStore
    (
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        return new EventPushNotificationDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IPresentationDataStore providesPresentationDataStore
    (
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        return new PresentationDataStore(saveOrUpdateStrategy, deleteStrategy);
    }


    @Provides
    IPresentationLinkDataStore providesPresentationLinkDataStore
    (
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        return new PresentationLinkDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IPresentationVideoDataStore providesPresentationVideoDataStore
    (
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        return new PresentationVideoDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IPresentationSlideDataStore providesPresentationSlideDataStore
    (
        ISaveOrUpdateStrategy saveOrUpdateStrategy,
        IDeleteStrategy deleteStrategy
    )
    {
        return new PresentationSlideDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IWifiConnectionDataStore providesWifiConnectionDataStore
            (
                    ISaveOrUpdateStrategy saveOrUpdateStrategy,
                    IDeleteStrategy deleteStrategy
            )
    {
        return new WifiConnectionDataStore(saveOrUpdateStrategy, deleteStrategy);
    }

    @Provides
    IMyScheduleProcessableUserActionDataStore providesMyScheduleProcessableUserActionDataStore(){
        return new MyScheduleProcessableUserActionDataStore();
    }

    @Provides
    IMyFavoriteProcessableUserActionDataStore providesMyFavoriteProcessableUserActionDataStore(){
        return new MyFavoriteProcessableUserActionDataStore();
    }

    @Provides
    IMyFeedbackProcessableUserActionDataStore providesMyFeedbackProcessableUserActionDataStore(){
        return new MyFeedbackProcessableUserActionDataStore();
    }

    @Provides
    IMyRSVPProcessableUserActionDataStore providesMyRSVPProcessableUserActionDataStore(){
        return new MyRSVPProcessableUserActionDataStore();
    }
}