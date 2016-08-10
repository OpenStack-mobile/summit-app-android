package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationVideo extends RealmObject implements IPresentationVideo {

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private boolean displayOnSite;
    private boolean featured;
    private int order;
    private Presentation presentation;
    private String youTubeId;
    private long views;
    private Date dateUploaded;
    private boolean highlighted;

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

    @Override
    public String getYouTubeId() {
        return youTubeId;
    }

    @Override
    public void setYouTubeId(String youTubeId) {
        this.youTubeId = youTubeId;
    }

    @Override
    public boolean isHighlighted() {
        return highlighted;
    }

    @Override
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public long getViews() {
        return views;
    }

    @Override
    public void setViews(long views) {
        this.views = views;
    }

    @Override
    public Date getDateUploaded() {
        return dateUploaded;
    }

    @Override
    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
}
