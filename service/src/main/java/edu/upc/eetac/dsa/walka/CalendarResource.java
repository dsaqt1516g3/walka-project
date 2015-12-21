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

    //Obtener por dia
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
