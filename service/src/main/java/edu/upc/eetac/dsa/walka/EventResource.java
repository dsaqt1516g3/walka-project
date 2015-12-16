package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.dao.EventDAO;
import edu.upc.eetac.dsa.walka.dao.EventDAOImpl;
import edu.upc.eetac.dsa.walka.entity.AuthToken;
import edu.upc.eetac.dsa.walka.entity.Event;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by SergioGM on 16.12.15.
 */
@Path("events")
public class EventResource {
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(WalkaMediaType.WALKA_EVENT)
    public Response createEvent(@FormParam("title") String title, @FormParam("location") String location, @FormParam("notes") String notes, @FormParam("start") long start,@FormParam("end") long end, @Context UriInfo uriInfo) throws URISyntaxException {
            if (title == null ||  start == 0 || end == 0 )
                throw new BadRequestException("Title and start date are mandatory");
            EventDAO eventDAO = new EventDAOImpl();
            Event event  = null;
            AuthToken authToken = null;


            try {
                event = eventDAO.createEvent(securityContext.getUserPrincipal().getName(), title, location, notes, start, end);
                String id = event.getId();
                PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("walka");
                String baseURI = prb.getString("walka.eventsurl");

                String eventurl = baseURI + "/" +id;
                event.setUrl(eventurl);
            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
            URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + event.getId());
            return Response.created(uri).type(WalkaMediaType.WALKA_EVENT).entity(event).build();


    }


}
