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
public class User {

        @InjectLinks({@InjectLink(resource = WalkaRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Walka Root API"),
                @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
                @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "self user-profile", title = "User profile", type= WalkaMediaType.WALKA_USER, bindings = @Binding(name = "id", value = "${instance.id}")),
                @InjectLink(resource = EventResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-event", title = "Create event", type= WalkaMediaType.WALKA_EVENT),
                @InjectLink(resource = CalendarResource.class, style = InjectLink.Style.ABSOLUTE, rel = "fill-calendar", title = "Events calendar", type= WalkaMediaType.WALKA_EVENT_COLLECTION)



        })
        private List<Link> links;
        private String id;
        private String loginid;
        private String email;
        private String fullname;
        private String country;
        private String city;
        private String phonenumber;
        private String birthdate;

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

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}
