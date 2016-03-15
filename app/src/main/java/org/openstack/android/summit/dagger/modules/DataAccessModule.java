package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.DataUpdateDataStore;
import org.openstack.android.summit.common.data_access.GenericDataStore;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.IMemberDataStore;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeRemoteDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventRemoteDataStore;
import org.openstack.android.summit.common.data_access.ISummitRemoteDataStore;
import org.openstack.android.summit.common.data_access.ITrackGroupDataStore;
import org.openstack.android.summit.common.data_access.MemberDataStore;
import org.openstack.android.summit.common.data_access.MemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.PresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.SummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.SummitAttendeeRemoteDataStore;
import org.openstack.android.summit.common.data_access.SummitDataStore;
import org.openstack.android.summit.common.data_access.SummitEventDataStore;
import org.openstack.android.summit.common.data_access.SummitEventRemoteDataStore;
import org.openstack.android.summit.common.data_access.SummitRemoteDataStore;
import org.openstack.android.summit.common.data_access.TrackGroupDataStore;
import org.openstack.android.summit.common.data_access.data_polling.ClassResolver;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdatePoller;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateProcessor;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdateStrategyFactory;
import org.openstack.android.summit.common.data_access.data_polling.IClassResolver;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdateProcessor;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdateStrategyFactory;
import org.openstack.android.summit.common.data_access.data_polling.MyScheduleDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.SummitDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.data_polling.TrackGroupDataUpdateStrategy;
import org.openstack.android.summit.common.data_access.deserialization.Deserializer;
import org.openstack.android.summit.common.data_access.deserialization.DeserializerStorage;
import org.openstack.android.summit.common.data_access.deserialization.FeedbackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.GenericDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializerStorage;
import org.openstack.android.summit.common.data_access.deserialization.IFeedbackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IGenericDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IMemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPersonDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IPresentationSpeakerDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ISummitEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ITrackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.ITrackGroupDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueRoomDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.MemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PersonDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationSpeakerDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.TrackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.TrackGroupDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueRoomDeserializer;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.network.Reachability;
import org.openstack.android.summit.common.security.ISecurityManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Module
public class DataAccessModule {

    @Provides
    @Singleton
    IDeserializerStorage providesDeserializerStorage() {
        return new DeserializerStorage(Realm.getDefaultInstance());
    }

    @Provides
    IPersonDeserializer providesPersonDeserializer() {
        return new PersonDeserializer();
    }

    @Provides
    ISummitEventDeserializer providesSummitEventDeserializer(IGenericDeserializer genericDeserializer, IPresentationDeserializer presentationDeserializer, IDeserializerStorage deserializerStorage) {
        return new SummitEventDeserializer(genericDeserializer, presentationDeserializer, deserializerStorage);
    }

    @Provides
    IVenueRoomDeserializer providesVenueRoomDeserializer(IDeserializerStorage deserializerStorage) {
        return new VenueRoomDeserializer(deserializerStorage);
    }

    @Provides
    IVenueDeserializer providesVenueDeserializer(IGenericDeserializer genericDeserializer, IDeserializerStorage deserializerStorage) {
        return new VenueDeserializer(genericDeserializer, deserializerStorage);
    }

    @Provides
    ISummitDeserializer providesSummitDeserializer(IGenericDeserializer genericDeserializer,
                                                   IVenueDeserializer venueDeserializer,
                                                   IVenueRoomDeserializer venueRoomDeserializer,
                                                   ISummitEventDeserializer summitEventDeserializer,
                                                   IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
                                                   ITrackGroupDeserializer trackGroupDeserializer,
                                                   ITrackDeserializer trackDeserializer,
                                                   IDeserializerStorage deserializerStorage) {
        return new SummitDeserializer(genericDeserializer, venueDeserializer, venueRoomDeserializer, summitEventDeserializer, presentationSpeakerDeserializer, trackGroupDeserializer, trackDeserializer, deserializerStorage);
    }

    @Provides
    IPresentationDeserializer providesPresentationDeserializer(IPresentationSpeakerDeserializer presentationSpeakerDeserializer, IDeserializerStorage deserializerStorage) {
        return new PresentationDeserializer(presentationSpeakerDeserializer, deserializerStorage);
    }

    @Provides
    ISummitAttendeeDeserializer providesSummitAttendeeDeserializer(IPersonDeserializer personDeserializer, IFeedbackDeserializer feedbackDeserializer, IDeserializerStorage deserializerStorage) {
        return new SummitAttendeeDeserializer(personDeserializer, feedbackDeserializer, deserializerStorage);
    }

    @Provides
    IPresentationSpeakerDeserializer providesPresentationSpeakerDeserializer(IPersonDeserializer personDeserializer, IDeserializerStorage deserializerStorage) {
        return new PresentationSpeakerDeserializer(personDeserializer, deserializerStorage);
    }

    @Provides
    ITrackDeserializer providesTrackDeserializerDeserializer(IDeserializerStorage deserializerStorage) {
        return new TrackDeserializer(deserializerStorage);
    }

    @Provides
    IFeedbackDeserializer providesFeedbackDeserializer(IDeserializerStorage deserializerStorage) {
        return new FeedbackDeserializer(deserializerStorage);
    }

    @Provides
    IGenericDeserializer providesGenericDeserializer(IDeserializerStorage deserializerStorage) {
        return new GenericDeserializer(deserializerStorage);
    }

    @Provides
    IMemberDeserializer providesMemberDeserializer(IPresentationSpeakerDeserializer presentationSpeakerDeserializer, ISummitAttendeeDeserializer summitAttendeeDeserializer) {
        return new MemberDeserializer(presentationSpeakerDeserializer, summitAttendeeDeserializer);
    }

    @Provides
    ITrackGroupDeserializer providesTrackGroupDeserializer(IDeserializerStorage deserializerStorage, ITrackDeserializer trackDeserializer) {
        return new TrackGroupDeserializer(deserializerStorage, trackDeserializer);
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
                                       IVenueDeserializer venueDeserializer)
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
                venueDeserializer);
    }

    @Provides
    ISummitRemoteDataStore providesSummitRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        return new SummitRemoteDataStore(httpTaskFactory, deserializer);
    }

    @Provides
    ISummitDataStore providesSummitDataStore(ISummitRemoteDataStore summitRemoteDataStore) {
        return new SummitDataStore(summitRemoteDataStore);
    }

    @Provides
    IMemberRemoteDataStore providesMemberRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        return new MemberRemoteDataStore(httpTaskFactory, deserializer);
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
    ISummitEventRemoteDataStore providesSummitEventRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        return new SummitEventRemoteDataStore(httpTaskFactory, deserializer);
    }

    @Provides
    ISummitAttendeeRemoteDataStore providesSummitAttendeeRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        return new SummitAttendeeRemoteDataStore(httpTaskFactory, deserializer);
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
    IDataUpdateDataStore providesDataUpdateDataStore() {
        return new DataUpdateDataStore();
    }

    @Provides
    IDataUpdateStrategyFactory providesDataUpdateStrategyFactory(IGenericDataStore genericDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, ISummitDataStore summitDataStore, ITrackGroupDataStore trackGroupDataStore, ISecurityManager securityManager) {
        return new DataUpdateStrategyFactory(
                new DataUpdateStrategy(genericDataStore),
                new MyScheduleDataUpdateStrategy(genericDataStore, summitAttendeeDataStore, securityManager),
                new SummitDataUpdateStrategy(genericDataStore, summitDataStore),
                new TrackGroupDataUpdateStrategy(genericDataStore, trackGroupDataStore)
        );
    }

    @Provides
    IDataUpdateProcessor providesDataUpdateProcessor(IDeserializer deserializer, IDataUpdateStrategyFactory dataUpdateStrategyFactory, IDataUpdateDataStore dataUpdateDataStore, IDeserializerStorage deserializerStorage) {
        return new DataUpdateProcessor(deserializer, dataUpdateStrategyFactory, dataUpdateDataStore, new ClassResolver(), deserializerStorage);
    }

    @Provides
    @Singleton
    IDataUpdatePoller providesDataUpdatePoller(ISecurityManager securityManager, IHttpTaskFactory httpTaskFactory, IDataUpdateProcessor dataUpdateProcessor, IDataUpdateDataStore dataUpdateDataStore, ISummitDataStore summitDataStore, ISession session) {
        return new DataUpdatePoller(securityManager, httpTaskFactory, dataUpdateProcessor, dataUpdateDataStore, summitDataStore, new Reachability(), session);
    }

    @Provides
    @Singleton
    IReachability providesReachability() {
        return new Reachability();
    }
}