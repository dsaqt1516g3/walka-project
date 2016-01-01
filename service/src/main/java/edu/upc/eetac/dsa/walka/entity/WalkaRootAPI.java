package edu.upc.eetac.dsa.walka.entity;

import edu.upc.eetac.dsa.walka.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by SergioGM on 28.12.15.
 */
public class WalkaRootAPI {

    @InjectLinks({
            @InjectLink(resource = WalkaRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self bookmark home", title = "Walka Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "login", title = "Login",  type= WalkaMediaType.WALKA_AUTH_TOKEN),
            @InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-user", title = "Register", type= WalkaMediaType.WALKA_AUTH_TOKEN),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout", condition="${!empty resource.userid}"),
            @InjectLink(resource = EventResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-event", title = "Create event", condition="${!empty resource.userid}", type=WalkaMediaType.WALKA_EVENT),
            @InjectLink(resource = UserResource.class, method="getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", condition="${!empty resource.userid}", type= WalkaMediaType.WALKA_USER, bindings = @Binding(name = "id", value = "${resource.userid}")),
            @InjectLink(resource = CalendarResource.class, style = InjectLink.Style.ABSOLUTE, rel = "fill-calendar", title = "Events calendar", condition="${!empty resource.userid}", type= WalkaMediaType.WALKA_EVENT_COLLECTION)


    })
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
