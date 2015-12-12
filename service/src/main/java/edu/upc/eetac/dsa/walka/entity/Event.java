package edu.upc.eetac.dsa.walka.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by SergioGM on 05.12.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

    @InjectLinks({})
    private List<Link> links;
    private String id;
    private String creator;
    private UserCollection participants;
    private String title;
    private String location;
    private String notes;
    private String url = null;
    private long startdate;
    private long enddate;
    private long lastModified;
    private long creationTimestamp;

    public List<Link> getLinks() {
        return links;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public UserCollection getParticipants() {
        return participants;
    }

    public void setParticipants(UserCollection participants) {
        this.participants = participants;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public long getEnddate() {
        return enddate;
    }

    public void setEnddate(long enddate) {
        this.enddate = enddate;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
