package org.openstack.android.summit.common.security.oidc;

/**
 * Created by sebastian on 7/20/2016.
 */
final public class AuthCodeRequest {

    private String url;

    public AuthCodeRequest(String url){
        this.url = url;
    }

    public String toString(){
        return this.url;
    }

}
