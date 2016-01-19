package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.dao.EventDAO;
import edu.upc.eetac.dsa.walka.dao.EventDAOImpl;
import edu.upc.eetac.dsa.walka.entity.EventCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.sql.SQLException;

/**
 * Created by SergioGM on 20.12.15.
 */
@Path("calendar")
public class CalendarResource {
    @Context
    private SecurityContext securityContext;


    //Obtener por mes
    @Path("/month")
    @GET
    @Produces(WalkaMediaType.WALKA_EVENT_COLLECTION)
    public EventCollection getEventsByMonth(@QueryParam("monthdate") String monthdate){
        System.out.println(monthdate);
        EventCollection eventCollection;
        EventDAO eventDAO = new EventDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();

        try {

            eventCollection = eventDAO.getEventsMonth(userid, monthdate);
            eventCollection.setFromDate(monthdate);
            eventCollection.setToDate("Interval 1 month");

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;


    }

    @Path("/between")
    @GET
    @Produces(WalkaMediaType.WALKA_EVENT_COLLECTION)
    public EventCollection getEventsBetween(@QueryParam("start") String startD, @QueryParam("end") String endD){
        EventCollection eventCollection;
        EventDAO eventDAO = new EventDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();

        try {
            eventCollection = eventDAO.getEventsBetween(userid, startD, endD);
            eventCollection.setFromDate(startD);
            eventCollection.setToDate(endD);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;
    }

    @Path("/week")
    @GET
    @Produces(WalkaMediaType.WALKA_EVENT_COLLECTION)
    public EventCollection getEventsWeek(@QueryParam("weekdate") String weekday){

        EventCollection eventCollection;
        EventDAO eventDAO = new EventDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();

        try {
            eventCollection = eventDAO.getEventsWeek(userid,weekday);
            eventCollection.setFromDate(weekday);
            eventCollection.setToDate("Interval 1 week");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;
    }


    @Path("/day")
    @GET
    @Produces(WalkaMediaType.WALKA_EVENT_COLLECTION)
    public EventCollection getEventsByDay(@QueryParam("daydate") String daydate){
        EventCollection eventCollection;
        EventDAO eventDAO = new EventDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();

        try {
            eventCollection = eventDAO.getEventsDay(userid, daydate);
            eventCollection.setFromDate(daydate);
            eventCollection.setToDate("Interval 1 day");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;

    }

}
