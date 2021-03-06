package edu.upc.eetac.dsa.walka.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.walka.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by SergioGM on 05.12.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

    @InjectLinks({
            @InjectLink(resource = WalkaRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Walka Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = UserResource.class, method="getUser", style = InjectLink.Style.ABSOLUTE, rel = "creator-profile", title = "Creator profile",  bindings = @Binding(name = "id", value = "${instance.creator}")),
            @InjectLink(resource = EventResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-event", title = "Create event", type= WalkaMediaType.WALKA_EVENT),
            @InjectLink(resource = EventResource.class, method = "getEvent", style = InjectLink.Style.ABSOLUTE, rel = "self event", title = "Event", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = EventResource.class, method = "updateEvent", style = InjectLink.Style.ABSOLUTE, rel = "edit-event", title = "Edit Event", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(value = "/events/{id}/participant", style = InjectLink.Style.ABSOLUTE, rel = "leaveEvent", title = "Leave event", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(value = "/events/{id}/participant/add", style = InjectLink.Style.ABSOLUTE, rel = "addUser", title = "Add user to event", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(value = "/events/{id}/participant/del/", style = InjectLink.Style.ABSOLUTE, rel = "deleteUser", title = "Delete user from event", bindings = @Binding(name = "id", value = "${instance.id}"))





    })
    private List<Link> links;
    private String id;
    private String creator;
    private String creatorName;
    private UserCollection participants;
    private String title;
    private String location;
    private String notes;
    private String tag;
    private String color;
    private String url;
    private String start;
    private String end;
    private long lastModified;
    private long creationTimestamp;

    public List<Link> getLinks() {
        return links;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String colour) {
        this.color = colour;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
