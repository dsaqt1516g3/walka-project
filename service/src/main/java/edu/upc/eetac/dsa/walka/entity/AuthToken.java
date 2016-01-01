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
public class AuthToken {
    @InjectLinks({@InjectLink(resource = WalkaRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Walka Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self login", title = "Login",  type= WalkaMediaType.WALKA_AUTH_TOKEN),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout", condition="${!empty instance.userid}"),
            @InjectLink(resource = EventResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-event", title = "Create event", condition="${!empty instance.userid}", type=WalkaMediaType.WALKA_EVENT),
            @InjectLink(resource = UserResource.class, method="getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", condition="${!empty instance.userid}", type= WalkaMediaType.WALKA_USER, bindings = @Binding(name = "id", value = "${instance.userid}")),
            @InjectLink(resource = CalendarResource.class, style = InjectLink.Style.ABSOLUTE, rel = "fill-calendar", title = "Events calendar", condition="${!empty instance.userid}", type= WalkaMediaType.WALKA_EVENT_COLLECTION)
            })
    private List<Link> links;

    private String userid;
    private String token;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
