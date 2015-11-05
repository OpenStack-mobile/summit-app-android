package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.Company;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/5/2015.
 */
public class CompanyDeserializer implements ICompanyDeserializer {
    @Inject INamedEntityDeserializer namedEntityDeserializer;

    @Inject
    public CompanyDeserializer() {}

    @Override
    public Company deserialize(JSONObject jsonObject) throws JSONException {
        Company entity = new Company();
        namedEntityDeserializer.deserialize(jsonObject, entity);
        return entity;
    }
}
