package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.dao.EventDAO;
import edu.upc.eetac.dsa.walka.dao.EventDAOImpl;
import edu.upc.eetac.dsa.walka.dao.UserDAO;
import edu.upc.eetac.dsa.walka.dao.UserDAOImpl;
import edu.upc.eetac.dsa.walka.entity.AuthToken;
import edu.upc.eetac.dsa.walka.entity.Event;
import edu.upc.eetac.dsa.walka.entity.EventCollection;
import edu.upc.eetac.dsa.walka.entity.User;

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
    public Response createEvent(@FormParam("title") String title, @FormParam("location") String location, @FormParam("notes") String notes, @FormParam("start") String start,@FormParam("end") String end, @Context UriInfo uriInfo) throws URISyntaxException {
            if (title == null ||  start == null )
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

        String userid = securityContext.getUserPrincipal().getName();
        CacheControl cacheControl = new CacheControl();
        Event event = null;
        EventDAO eventDAO = new EventDAOImpl();
        try {

            //Si el id de evento no existe
            event = eventDAO.getEventbyId(id);

            if (event == null)
            throw new NotFoundException("Event with id = " + id + "not found");

            //Si no pertenece al evento, no puede obtenerlo

           if(!eventDAO.checkUserInEvent(id,userid))
              throw new ForbiddenException("You are not in the event");


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

    @Path("/{id}")
    @PUT
    @Consumes(WalkaMediaType.WALKA_EVENT)
    @Produces(WalkaMediaType.WALKA_EVENT)
    public Event updateEvent(@PathParam("id") String id, Event event) {
        EventDAO eventDAO = new EventDAOImpl();
        if (event == null)
            throw new BadRequestException("Entity is null");
        if (!id.equals(event.getId()))
            throw new BadRequestException("Path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();

        try {
            event = eventDAO.updateEvent(id, event.getTitle(), event.getLocation(), event.getNotes(), event.getStart(), event.getEnd());

            if (event == null)
                throw new NotFoundException("Sting with id = " + id + " doesn't exist");
            //Debe ir filtro de si estas en el evento
            if(!eventDAO.checkUserInEvent(id, userid))
                throw new ForbiddenException("You are not in the event");

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return event;
    }

    @Path("/{id}")
    @DELETE
    public void deleteEvent(@PathParam("id") String id) {
        System.out.println(id);
        String userid = securityContext.getUserPrincipal().getName();
        EventDAO eventDAO = new EventDAOImpl();
        Event event = null;
        System.out.println("He entrado");
        System.out.println(userid);


        try {

            event = eventDAO.getEventbyId(id);
            String creator = event.getCreator();
            System.out.println(creator);


            if (event == null)
                throw new NotFoundException("Event with id = " + id + "not found");

            if (!userid.equals(creator))
                throw new ForbiddenException("You are not the creator");

            if (!eventDAO.deleteEvent(event.getId()))
                throw new NotFoundException("Couldn't delete event:  " + id);

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }


    //Dejar evento
    @Path("/{id}/participant")
    @DELETE
    public void leaveEvent(@PathParam("id") String id){
        String userid = securityContext.getUserPrincipal().getName();
        EventDAO eventDAO = new EventDAOImpl();
        Event event = null;

        try {

            event = eventDAO.getEventbyId(id);
            String creator = event.getCreator();
            System.out.println(userid);
            System.out.println(id);

            if (event == null)
                throw new NotFoundException("Event with id = " + id + " not found");

            if(!eventDAO.checkUserInEvent(id, userid))
                throw new ForbiddenException("You are not in the event");

            if (userid.equals(creator))
                throw new ForbiddenException("Cannot leave the event if you are the creator. Delete it");

            if (!eventDAO.LeaveEvent(userid, id))
                throw new NotFoundException("Couldn't leave event:  " + id);



        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    //Add user to event
    @Path("/{id}/participant")
    @POST
    @Produces(WalkaMediaType.WALKA_USER)
    public User addUserToEvent(@PathParam("id") String id, @FormParam("iduser") String userlogin){
        String userid = securityContext.getUserPrincipal().getName();
        EventDAO eventDAO = new EventDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        Event event = null;
        User user = null;

        try {

            event = eventDAO.getEventbyId(id);
            String creator = event.getCreator();
            user = userDAO.getUserByLoginid(userlogin);


            if (event == null)
                throw new NotFoundException("Event with id = " + id + " not found");
            if (user == null)
                throw new NotFoundException("User with login = " + userlogin + " doesn't exists");

            if (!userid.equals(creator))
                throw new ForbiddenException("Only the creator can add participants");

            if (!eventDAO.JoinEvent(user.getId(), id))
                throw new NotFoundException("Couldn't add participant to event, probably the user is already in the event: " + id);



        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return user;


    }

    //Get all events from user
    @GET
    @Produces(WalkaMediaType.WALKA_EVENT_COLLECTION)
    public EventCollection getEventsUser(){

        EventCollection eventCollection;
        EventDAO eventDAO = new EventDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();

        try {
            eventCollection = eventDAO.getAllEvents(userid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;
    }



/**

    @Path("/{id}/participants")
    @POST
    @Consumes(WalkaMediaType.WALKA_LOGIN_COLLECTION)
    @Produces(WalkaMediaType.WALKA_USER_COLLECTION)
    public UserCollection addParticipantsToEvent(@PathParam("id") String idevent, LoginUsersCollection logins){
        String userid = securityContext.getUserPrincipal().getName();
        UserDAO userDAO = new UserDAOImpl();
        EventDAO eventDAO = new EventDAOImpl();
        UserCollection participantsAdded;
        Event event = null;
        try {

            event = eventDAO.getEventbyId(idevent);
            String creator = event.getCreator();

            if (event == null)
                throw new NotFoundException("Event with id = " + idevent + "not found");

            if (!userid.equals(creator))
                throw new ForbiddenException("Only the creator can add participants");

            for(String login: )){

            }

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

    }

*/



}
