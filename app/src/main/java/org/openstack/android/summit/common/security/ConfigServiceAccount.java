package org.openstack.android.summit.common.security;

import org.openstack.android.summit.common.Constants;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class ConfigServiceAccount {
    // TODO: Add the information you received from your OIDC provider below.
    public static final String TEST_CLIENT_ID = "JYvz.AKTzlpLfSl4G.i42Abp-e5dVGVn.openstack.client";
    public static final String TEST_CLIENT_SECRET = "lQxHMrd576TK.iLXFXVK7mpxY1hOie~gD71T_2H1o7kR0GlstSeomMU~rfMfy_Vy";
    public static final String PRODUCTION_CLIENT_ID = "h70UpXbSlOAx-i.pYxPDM7qE03FW2nWF.openstack.client";
    public static final String PRODUCTION_CLIENT_SECRET = "RlL3T1Cw5mugUs05ZYlc63hEwgEVgRTpXx_sASex5rZrh2kTpEGWs8VkiGfIZt5u";

    // The `offline_access` scope enables us to request Refresh Tokens, so we don't have to ask the
    // user to authorise us again every time the tokens expire. Some providers might have an
    // `offline` scope instead. If you get an `invalid_scope` error when trying to authorise the
    // app, try changing it to `offline`.
    public static final String[] SCOPES = {"https://testresource-server.openstack.org/summits/read"};
}
