package edu.upc.eetac.dsa.walka.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.walka.CalendarResource;
import edu.upc.eetac.dsa.walka.LoginResource;
import edu.upc.eetac.dsa.walka.WalkaMediaType;
import edu.upc.eetac.dsa.walka.WalkaRootAPIResource;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SergioGM on 05.12.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventCollection {
    @InjectLinks({
            @InjectLink(resource = WalkaRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Walka Root API"),
           @InjectLink(resource = CalendarResource.class,style = InjectLink.Style.ABSOLUTE, rel = "fill-calendar", title = "Events calendar", type= WalkaMediaType.WALKA_EVENT_COLLECTION),
           @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout")
    })
    private List<Link> links;
    private String fromDate;
    private String toDate;
    private List<Event> events = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
