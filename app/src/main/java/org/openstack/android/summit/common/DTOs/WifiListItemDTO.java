package org.openstack.android.summit.common.DTOs;

/**
 * Created by Claudio Redi on 11/18/2015.
 */

public class WifiListItemDTO {
    private int id = 0;
    private String ssid;
    private String password;
    private int summitId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSummitId() {
        return summitId;
    }

    public void setSummitId(int summitId) {
        this.summitId = summitId;
    }

}
