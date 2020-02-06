package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.entities.notifications.IPushNotification;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sebastian on 8/20/2016.
 */
abstract public class AbstractPushNotification2PushNotificationListItemDTO
        <S extends IPushNotification, D extends PushNotificationListItemDTO> extends AbstractConverter<S, D>
{
    @Override
    protected D convert(S source) {
        D dto = createDTO();
        dto.setId(source.getId());
        dto.setSubject(source.getTitle());
        dto.setBody(source.getBody());
        dto.setOpened(source.isOpened());
        // convert from utc to local device timezone
        DateTimeZone localTimeZone = DateTimeZone.forID(TimeZone.getDefault().getID());
        Date utcCreatedAt          = source.getCreatedAt();
        DateTime localTime         = new DateTime(utcCreatedAt.getTime(), localTimeZone);
        dto.setReceivedDate(localTime.toDate());
        dto.setType(source.getChannel());
        if(source.getEvent() != null)
            dto.setEventId(source.getEvent().getId());
        return dto;
    }

    abstract protected  D createDTO();
}
