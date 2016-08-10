package org.openstack.android.summit.common.entities;

/**
 * Created by sebastian on 8/10/2016.
 */
public interface IPresentationMaterial extends IEntity {

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    boolean getDisplayOnSite();
    void setDisplayOnSite(boolean displayOnSite);

    boolean getFeatured();
    void setFeatured(boolean featured);

    int getOrder();
    void setOrder(int order);

    Presentation getPresentation();
    void setPresentation(Presentation presentation);

}
