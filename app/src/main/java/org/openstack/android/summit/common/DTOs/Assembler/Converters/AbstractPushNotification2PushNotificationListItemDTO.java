package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.entities.PushNotification;

/**
 * Created by sebastian on 8/20/2016.
 */
abstract public class AbstractPushNotification2PushNotificationListItemDTO
        <S extends PushNotification, D extends PushNotificationListItemDTO> extends AbstractConverter<S, D>
{
    @Override
    protected D convert(S source) {
        D dto = createDTO();
        dto.setId(source.getId());
        dto.setSubject(source.getSubject());
        dto.setBody(source.getBody());
        dto.setOpened(source.isOpened());
        dto.setReceivedDate(source.getReceived());
        dto.setEventId(source.getEventId());
        dto.setType(source.getType());
        return dto;
    }

    abstract protected  D createDTO();
}
