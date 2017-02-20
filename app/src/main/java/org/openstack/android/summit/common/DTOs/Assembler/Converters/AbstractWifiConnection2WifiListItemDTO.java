package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;
import com.crashlytics.android.Crashlytics;
import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.WifiListItemDTO;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitWIFIConnection;

/**
 * Created by Claudio Redi on 1/29/2016.
 */
public class AbstractWifiConnection2WifiListItemDTO<S extends SummitWIFIConnection> extends AbstractConverter<S, WifiListItemDTO> {
    @Override
    protected WifiListItemDTO convert(S source) {
        WifiListItemDTO wifiListItemDTO = new WifiListItemDTO();
        try {
            wifiListItemDTO.setId(source.getId());
            wifiListItemDTO.setSsid(source.getSsid());
            wifiListItemDTO.setPassword(source.getPassword());
            Summit summit    = source.getSummit();

            if(summit != null){
                wifiListItemDTO.setSummitId(summit.getId());
            }

        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return wifiListItemDTO;
    }

}
