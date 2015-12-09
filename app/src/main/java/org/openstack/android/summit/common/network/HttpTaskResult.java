package org.openstack.android.summit.common.network;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class HttpTaskResult {
    private Boolean succeed;
    private String body;

    public Boolean getSucceed() {
        return succeed;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
