package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import io.realm.SummitEventRealmProxy;

/**
 * Created by Claudio Redi on 12/19/2015.
 */
public class SummitEventRealmProxy2ScheduleItemDTO extends AbstractSummitEvent2ScheduleItemDTO<SummitEventRealmProxy> {
}