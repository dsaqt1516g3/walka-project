package edu.upc.eetac.dsa.walka.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by SergioGM on 08.01.16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {
    @InjectLinks({
            @InjectLink(value = "/groups/{id}/leave", style = InjectLink.Style.ABSOLUTE, rel = "leavegroup", title = "Leave group", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(value = "/groups/{id}/participant/add", style = InjectLink.Style.ABSOLUTE, rel = "inviteuser", title = "Invite user", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(value = "/groups/{id}/participant/del/", style = InjectLink.Style.ABSOLUTE, rel = "deleteUser", title = "Delete user from group", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(value = "/groups/{id}", style = InjectLink.Style.ABSOLUTE, rel = "edit-delete-self", title = "Edit Delete Self", bindings = @Binding(name = "id", value = "${instance.id}"))

    })
    private List<Link> links;
    private String id;
    private String creator;
    private String creatorName;
    private String name;
    private String description;
    private UserCollection components;
    private long creationTimestamp;
    private long lastModified;

    public List<Link> getLinks() {
        return links;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserCollection getComponents() {
        return components;
    }

    public void setComponents(UserCollection components) {
        this.components = components;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
