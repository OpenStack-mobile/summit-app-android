package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.entities.notifications.IEventPushNotification;
import org.openstack.android.summit.common.entities.notifications.IPushNotification;

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
        dto.setReceivedDate(source.getCreatedAt());
        dto.setType(source.getChannel());
        if(source instanceof IEventPushNotification){
            dto.setEventId(((IEventPushNotification) source).getEvent().getId());
        }
        return dto;
    }

    abstract protected  D createDTO();
}
