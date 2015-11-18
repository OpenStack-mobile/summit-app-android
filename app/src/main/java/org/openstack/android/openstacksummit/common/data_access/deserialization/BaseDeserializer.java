package org.openstack.android.openstacksummit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public abstract class BaseDeserializer {
    protected String[] validateRequiredFields(String[] fields, JSONObject jsonObject) {
        ArrayList<String> missedFields = new ArrayList<>();
        for(String field: fields) {
            if (jsonObject.isNull(field)) {
                missedFields.add(field);
            }
        }

        return missedFields.toArray(new String[0]);
    }

    protected void handleMissedFieldsIfAny(String[] missedFields) throws JSONException {
        if (missedFields.length > 0) {
            throw new JSONException("Following fields are missed " + TextUtils.join(", ", missedFields));
        }
    }
}
