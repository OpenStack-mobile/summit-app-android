package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;

import java.security.spec.InvalidParameterSpecException;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class SummitAttendeeRemoteDataStore implements ISummitAttendeeRemoteDataStore {
    private IDeserializer deserializer;
    private IHttpTaskFactory httpTaskFactory;

    @Inject
    public SummitAttendeeRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        this.httpTaskFactory = httpTaskFactory;
        this.deserializer = deserializer;
    }

    @Override
    public void addEventToShedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {
        addOrRemoveEventFromSchedule(summitAttendee, summitEvent, dataStoreOperationListener, HttpRequest.METHOD_POST);
    }

    @Override
    public void removeEventFromShedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {
        addOrRemoveEventFromSchedule(summitAttendee, summitEvent, dataStoreOperationListener, HttpRequest.METHOD_DELETE);
    }

    private void addOrRemoveEventFromSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent, final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener, String httpMethod) {
        final SummitAttendee finalSummitAttendee = summitAttendee;

        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                dataStoreOperationListener.onSuceedWithData(finalSummitAttendee);
            }

            @Override
            public void onError(String error) {
                dataStoreOperationListener.onError(error);
            }
        };

        String url = Constants.RESOURCE_SERVER_BASE_URL +
                String.format("/api/v1/summits/current/attendees/%s/schedule/%s", summitAttendee.getId(), summitEvent.getId());
        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.Create(AccountType.OIDC, url, httpMethod, httpTaskListener);
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        httpTask.execute();
    }

    /*var deserializerFactory: DeserializerFactory!
    var httpFactory: HttpFactory!

    public func getByFilter(searchTerm: String?, page: Int, objectsPerPage: Int, completionBlock : ([SummitAttendee]?, NSError?) -> Void) {
        let http = httpFactory.create(HttpType.ServiceAccount)

        var filter = ""
        if (searchTerm != nil && !(searchTerm!.isEmpty)) {
            filter = "filter=first_name=@\(searchTerm!),last_name=@\(searchTerm!)&"
        }

        http.GET("https://testresource-server.openstack.org/api/v1/summits/current/attendees?\(filter)page=\(page)&per_page=\(objectsPerPage)") {(responseObject, error) in
            if (error != nil) {
                completionBlock(nil, error)
                return
            }

            let json = responseObject as! String
            let deserializer : IDeserializer!
                    var innerError: NSError?
                    var attendees: [SummitAttendee]?

            deserializer = self.deserializerFactory.create(DeserializerFactoryType.SummitAttendee)

            do {
                attendees = try deserializer.deserializePage(json) as? [SummitAttendee]
            }
            catch {
                innerError = NSError(domain: "There was an error deserializing summit attendees", code: 4001, userInfo: nil)
            }

            completionBlock(attendees, innerError)
        }
    }

    public func getById(id: Int, completionBlock : (SummitAttendee?, NSError?) -> Void) {
        let http = httpFactory.create(HttpType.ServiceAccount)

        http.GET("https://testresource-server.openstack.org/api/v1/summits/current/attendees/\(id)") {(responseObject, error) in
            if (error != nil) {
                completionBlock(nil, error)
                return
            }

            let json = responseObject as! String
            let deserializer : IDeserializer!
                    var innerError: NSError?
                    var attendee: SummitAttendee?

                    deserializer = self.deserializerFactory.create(DeserializerFactoryType.SummitAttendee)

            do {
                attendee = try deserializer.deserialize(json) as? SummitAttendee
            }
            catch {
                innerError = NSError(domain: "There was an error deserializing summit attendee", code: 4002, userInfo: nil)
            }

            completionBlock(attendee, innerError)
        }
    }

    public func addFeedback(attendee: SummitAttendee, feedback: Feedback, completionBlock : (Feedback?, NSError?)->Void) {
        let endpoint = "https://testresource-server.openstack.org/api/v1/summits/current/events/\(feedback.event.id)/feedback"
        let http = httpFactory.create(HttpType.OpenIDJson)
        var jsonDictionary = [String:AnyObject]()
        jsonDictionary["rate"] = feedback.rate
        jsonDictionary["note"] = feedback.review
        jsonDictionary["attendee_id"] = attendee.id

        http.POST(endpoint, parameters: jsonDictionary, completionHandler: {(responseObject, error) in
            if (error != nil) {
                completionBlock(nil, error)
                return
            }

            let id = Int(responseObject as! String)!
                    feedback.id = id
            completionBlock(feedback, error)
        })
    }

    public func addEventToShedule(attendee: SummitAttendee, event: SummitEvent, completionBlock : (NSError?) -> Void) {
        let endpoint = "https://testresource-server.openstack.org/api/v1/summits/current/attendees/\(attendee.id)/schedule/\(event.id)"
        let http = httpFactory.create(HttpType.OpenIDGetFormUrlEncoded)
        //json[""]
        http.POST(endpoint, parameters: nil, completionHandler: {(responseObject, error) in
            if (error != nil) {
                completionBlock(error)
                return
            }
            completionBlock(error)
        })
    }

    public func removeEventFromShedule(attendee: SummitAttendee, event: SummitEvent, completionBlock : (NSError?) -> Void) {
        let endpoint = "https://testresource-server.openstack.org/api/v1/summits/current/attendees/\(attendee.id)/schedule/\(event.id)"
        let http = httpFactory.create(HttpType.OpenIDGetFormUrlEncoded)
        http.DELETE(endpoint, parameters: nil, completionHandler: {(responseObject, error) in
            if (error != nil) {
                completionBlock(error)
                return
            }
            completionBlock(error)
        })
    }*/
}
