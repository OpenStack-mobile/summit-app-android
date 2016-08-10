package org.openstack.android.summit.common.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationLink extends RealmObject implements IPresentationLink  {

    @PrimaryKey
    private int id;
    private String link;
    private String name;
    private String description;
    private boolean displayOnSite;
    private boolean featured;
    private int order;
    private Presentation presentation;

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean getDisplayOnSite() {
        return displayOnSite;
    }

    @Override
    public void setDisplayOnSite(boolean displayOnSite) {
        this.displayOnSite = displayOnSite;
    }

    @Override
    public boolean getFeatured() {
        return featured;
    }

    @Override
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public Presentation getPresentation() {
        return presentation;
    }

    @Override
    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
