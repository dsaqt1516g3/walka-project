package edu.upc.eetac.dsa.walka.client.entity;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marta_ on 06/01/2016.
 */
public class EventCollection {
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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    public String getFromDate() {
        return fromDate;
    }
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    public String getToDate() {
        return toDate;
    }
}
