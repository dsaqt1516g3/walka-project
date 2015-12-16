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
                //Genero url
                PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("walka");
                String baseURI = prb.getString("walka.eventsurl");
                String eventurl = baseURI + "/" +id;
                event.setUrl(eventurl);
                //Introduzco en user_events
                eventDAO.JoinEvent(securityContext.getUserPrincipal().getName(), event.getId());

            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
            URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + event.getId());
            return Response.created(uri).type(WalkaMediaType.WALKA_EVENT).entity(event).build();


    }

    @Path("/{id}")
    @GET
    @Produces(WalkaMediaType.WALKA_EVENT)
    public Response getEvent(@PathParam("id") String id, @Context Request request) {
        // Create cache-control
        //String userid = securityContext.getUserPrincipal().getName();
        CacheControl cacheControl = new CacheControl();
        Event event = null;
        EventDAO eventDAO = new EventDAOImpl();
        try {
            //Si el id de evento no existe
            event = eventDAO.getEventbyId(id);
            if (event == null)
            throw new NotFoundException("Event with id = " + id + "not found");

            //Si no pertenece al evento, no puede obtenerlo
            //if(!eventDAO.checkUserInEvent(id,userid))
               // throw new ForbiddenException("operation not allowed");


            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(event.getLastModified()));

            // Verify if it matched with etag available in http request
            Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

            // If ETag matches the rb will be non-null;
            // Use the rb to return the response without any further processing
            if (rb != null) {
                return rb.cacheControl(cacheControl).tag(eTag).build();
            }

            // If rb is null then either it is first time request; or resource is
            // modified
            // Get the updated representation and return with Etag attached to it
            rb = Response.ok(event).cacheControl(cacheControl).tag(eTag);
            return rb.build();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }


}
