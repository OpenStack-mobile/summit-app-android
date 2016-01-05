package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.data_access.GenericDataStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.IMemberDataStore;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.ISummitRemoteDataStore;
import org.openstack.android.summit.common.data_access.MemberDataStore;
import org.openstack.android.summit.common.data_access.MemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.SummitDataStore;
import org.openstack.android.summit.common.data_access.SummitEventDataStore;
import org.openstack.android.summit.common.data_access.SummitRemoteDataStore;
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
import org.openstack.android.summit.common.data_access.deserialization.IVenueDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IVenueRoomDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.MemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PersonDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationSpeakerDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.VenueRoomDeserializer;
import org.openstack.android.summit.common.network.IHttpTaskFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Module
public class DataAccessModule {

    @Provides
    @Singleton
    IDeserializerStorage providesDeserializerStorage() {
        return new DeserializerStorage();
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
                                                   IDeserializerStorage deserializerStorage) {
        return new SummitDeserializer(genericDeserializer, venueDeserializer, venueRoomDeserializer, summitEventDeserializer, presentationSpeakerDeserializer, deserializerStorage);
    }

    @Provides
    IPresentationDeserializer providesPresentationDeserializer(IDeserializerStorage deserializerStorage) {
        return new PresentationDeserializer(deserializerStorage);
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
    IDeserializer providesDeserializer(IGenericDeserializer genericDeserializer,
                                       IFeedbackDeserializer feedbackDeserializer,
                                       IMemberDeserializer memberDeserializer,
                                       IPresentationDeserializer presentationDeserializer,
                                       IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
                                       ISummitAttendeeDeserializer summitAttendeeDeserializer,
                                       ISummitDeserializer summitDeserializer,
                                       ISummitEventDeserializer summitEventDeserializer) {
        return new Deserializer(genericDeserializer,
                feedbackDeserializer,
                memberDeserializer,
                presentationDeserializer,
                presentationSpeakerDeserializer,
                summitAttendeeDeserializer,
                summitDeserializer,
                summitEventDeserializer);
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
    IMemberDataStore providesMemberDataStore(IMemberRemoteDataStore memberRemoteDataStore, IGenericDataStore genericDataStore) {
        return new MemberDataStore(memberRemoteDataStore, genericDataStore);
    }

    @Provides
    ISummitEventDataStore providesSummitEventDataStore() {
        return new SummitEventDataStore();
    }
}