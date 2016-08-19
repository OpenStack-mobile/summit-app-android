package org.openstack.android.summit.common.api_endpoints;

import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sebastian on 8/18/2016.
 */
final public class ApiEndpointBuilder {

    public static final String EventIdParam            = "event_id";
    public static final String OrderNumberParam        = "order_nbr";
    public static final String ExternalAttendeeIdParam = "external_attendee_id";
    public static final String ExpandParam             = "expand";
    public static final String FieldsParam             = "fields";
    public static final String PageParam               = "page";
    public static final String PerPageParam            = "per_page";
    public static final String RelationsParam          = "relations";
    public static final String LimitParam              = "limit";
    public static final String FromDateParam           = "from_date";
    public static final String LastEventIdParam        = "last_event_id";

    private ApiEndpointBuilder() {}
    private static final Object lock = new Object();
    private static ApiEndpointBuilder instance;

    public static ApiEndpointBuilder getInstance() {
        synchronized (lock) {
            if (instance == null)
                instance = new ApiEndpointBuilder();
            return instance;
        }
    }

    public enum EndpointType{
        GetSummit,
        EntityEvents,
        GetMemberInfo,
        RemoveAddFromMySchedule,
        GetExternalOrder,
        ConfirmExternalOrder,
        GetPublishedEvent,
        GetFeedback,
        AddFeedback
    }

    static private Uri buildURI(String url, Map<String, Object> params) {

        // build url with parameters.
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue().toString());
        }

        return builder.build();
    }

    private final String EntityEventsEndpoint            = "/api/v1/summits/%s/entity-events";
    private final String GetMemberInfoEndpoint           = "/api/v1/summits/%s/members/me";
    private final String RemoveAddFromMyScheduleEndpoint = "/api/v1/summits/%s/attendees/me/schedule/%s";
    private final String GetExternalOrderEndpoint        = "/api/v1/summits/%s/external-orders/%s";
    private final String ConfirmExternalOrderEndpoint    = "/api/v1/summits/%s/external-orders/%s/external-attendees/%d/confirm";
    private final String GetPublishedEventEndpoint       = "/api/v1/summits/%s/events/%s/published";
    private final String GetFeedbackEndpoint             = "/api/v1/summits/%s/events/%s/feedback";
    private final String GetSummitEndpoint               = "/api/v1/summits/%s";
    private final String AddFeedbackEndpoint             = "/api/v2/summits/%s/events/%s/feedback";

    public Uri buildEndpoint(String baseUrl, String summit_id, EndpointType type){
        return buildEndpoint(baseUrl, summit_id, type, new HashMap<String, Object>());
    }

    public Uri buildEndpoint(String baseUrl, String summit_id, EndpointType type, Map<String, Object> params){

        switch(type){
            case GetSummit:
                return buildURI(String.format(baseUrl+GetSummitEndpoint, summit_id), params);
            case EntityEvents:
                return buildURI(String.format(baseUrl+EntityEventsEndpoint, summit_id), params);
            case GetMemberInfo:
                return buildURI(String.format(baseUrl+GetMemberInfoEndpoint, summit_id), params);
            case RemoveAddFromMySchedule: {
                Object eventId = params.get(EventIdParam);
                params.remove(EventIdParam);
                return buildURI(String.format(baseUrl + RemoveAddFromMyScheduleEndpoint, summit_id, eventId), params);
            }
            case GetExternalOrder: {
                Object orderNbr = params.get(OrderNumberParam);
                params.remove(OrderNumberParam);
                return buildURI(String.format(baseUrl + GetExternalOrderEndpoint, summit_id, orderNbr), params);
            }
            case ConfirmExternalOrder: {
                Object orderNbr           = params.get(OrderNumberParam);
                Object externalAttendeeId = params.get(ExternalAttendeeIdParam);
                params.remove(OrderNumberParam);
                params.remove(ExternalAttendeeIdParam);
                return buildURI(String.format(baseUrl + ConfirmExternalOrderEndpoint, summit_id, orderNbr, externalAttendeeId), params);
            }
            case GetPublishedEvent: {
                Object eventId = params.get(EventIdParam);
                params.remove(EventIdParam);
                return buildURI(String.format(baseUrl + GetPublishedEventEndpoint, summit_id, eventId), params);
            }
            case GetFeedback: {
                Object eventId = params.get(EventIdParam);
                params.remove(EventIdParam);
                return buildURI(String.format(baseUrl + GetFeedbackEndpoint, summit_id, eventId), params);
            }
            case AddFeedback: {
                Object eventId = params.get(EventIdParam);
                params.remove(EventIdParam);
                return buildURI(String.format(baseUrl + AddFeedbackEndpoint, summit_id, eventId), params);
            }
        }
        return null;
    }
}
