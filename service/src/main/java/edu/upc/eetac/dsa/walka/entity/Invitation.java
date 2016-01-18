package edu.upc.eetac.dsa.walka.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by SergioGM on 09.01.16.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Invitation {
    @InjectLinks({
            @InjectLink(value = "/groups/{idgroup}/invitations/accept", style = InjectLink.Style.ABSOLUTE, rel = "acceptinvitation", title = "Accept invitation", bindings = @Binding(name = "idgroup", value = "${instance.idgroup}")),
            @InjectLink(value = "/groups/{idgroup}/invitations/refuse", style = InjectLink.Style.ABSOLUTE, rel = "refuseinvitation", title = "Refuse invitation", bindings = @Binding(name = "idgroup", value = "${instance.idgroup}"))


    })
    private List<Link> links;
    private Group groupInvitator;
    private String idgroup;
    private String userInvitedId;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Group getGroupInvitator() {
        return groupInvitator;
    }

    public void setGroupInvitator(Group groupInvitator) {
        this.groupInvitator = groupInvitator;
    }

    public String getIdgroup() {
        return idgroup;
    }

    public void setIdgroup(String idgroup) {
        this.idgroup = idgroup;
    }

    public String getUserInvitedId() {
        return userInvitedId;
    }

    public void setUserInvitedId(String userInvitedId) {
        this.userInvitedId = userInvitedId;
    }
}
