package com.edaviessmith.consumecontent.data;

import java.util.ArrayList;
import java.util.List;

public class User extends Content{


	public List<MediaFeed> mediaFeed;

    private boolean isNotification;

    private List<Group> groups; //TODO need to thoroughly hash out this relationship

    public User () {
        mediaFeed = new ArrayList<MediaFeed>();
        groups = new ArrayList<Group>();
        isNotification = true;
    }

    public User(int id, int sort, String name, String thumbnail, /*int youtubeChannelId, int twitterFeedId,*/ boolean isNotification) {
        super(id, sort, name, thumbnail);
        setNotification(isNotification);

    }


    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean isNotification) {
        this.isNotification = isNotification;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<MediaFeed> getMediaFeed() {
        return mediaFeed;
    }

    public void setMediaFeed(List<MediaFeed> mediaFeed) {
        this.mediaFeed = mediaFeed;
    }
}