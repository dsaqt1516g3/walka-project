package edu.upc.eetac.dsa.walka.entity;

import edu.upc.eetac.dsa.walka.LoginResource;
import edu.upc.eetac.dsa.walka.UserResource;
import edu.upc.eetac.dsa.walka.WalkaMediaType;
import edu.upc.eetac.dsa.walka.WalkaRootAPIResource;
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

    })
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
