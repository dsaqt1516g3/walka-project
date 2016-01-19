package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.dao.*;
import edu.upc.eetac.dsa.walka.entity.*;

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
    public Response createEvent(@FormParam("title") String title, @FormParam("location") String location, @FormParam("notes") String notes, @FormParam("start") String start,@FormParam("end") String end, @FormParam("tag") String tag , @FormParam("colour") String colour, @Context UriInfo uriInfo) throws URISyntaxException {
            if (title == null ||  start == null || end == null )
                throw new BadRequestException("Titulo y fechas son obligatorias");


            EventDAO eventDAO = new EventDAOImpl();

            Event event  = null;
            AuthToken authToken = null;
            System.out.println(title);
            System.out.println(tag);
            System.out.println(location);
            System.out.println(notes);
            System.out.println(start);
            System.out.println(end);

            try {
                event = eventDAO.createEvent(securityContext.getUserPrincipal().getName(), title, location, notes, tag, start, end);
                String id = event.getId();
                System.out.println(id);
                //Genero url
                PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("walka");
                String baseURI = prb.getString("walka.eventsurl");
                String eventurl = baseURI + "/" +id;
                event.setUrl(eventurl);
                System.out.println(eventurl);
                //Introduzco en user_events
                eventDAO.JoinEvent(securityContext.getUserPrincipal().getName(), event.getId());
                eventDAO.modifyColour(colour, event.getId(), securityContext.getUserPrincipal().getName());
                event.setColor(eventDAO.getColour(event.getId(), securityContext.getUserPrincipal().getName()));

            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
            URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + event.getId());
            return Response.created(uri).type(WalkaMediaType.WALKA_EVENT).entity(event).build();


    }

    @Path("/{id}")
    @GET
    @Produces(WalkaMediaType.WALKA_EVENT)
    public Event getEvent(@PathParam("id") String id, @Context Request request) {
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
              throw new ForbiddenException("No estas en el evento");

            event.setColor(eventDAO.getColour(id,userid));

/*
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
            */
            return event;
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


            Event checkEvent = eventDAO.getEventbyId(event.getId());

            if (checkEvent == null)
                throw new NotFoundException("Event with id = " + id + "not found");


            if(!eventDAO.checkUserInEvent(id, userid))
                throw new ForbiddenException("No formas parte del evento.");
            System.out.println(event.getTitle());
            System.out.println(event.getColor());

            eventDAO.modifyColour(event.getColor(),id ,userid);
            String colour = event.getColor();
            event = eventDAO.updateEvent(id, event.getTitle(), event.getLocation(), event.getNotes(), event.getTag(), event.getStart(), event.getEnd());
            event.setColor(colour);





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
                throw new ForbiddenException("No puedes borrar el evento si no eres el creador");

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
                throw new ForbiddenException("No puedes dejar el evento si eres su creador.  Puedes borrarlo si así lo deseas");

            if (!eventDAO.LeaveEvent(userid, id))
                throw new NotFoundException("Couldn't leave event:  " + id);



        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    //Add user to event
    @Path("/{id}/participant/add")
    @POST
    @Produces(WalkaMediaType.WALKA_USER)
    public User addUserToEvent(@PathParam("id") String id, @FormParam("loginuser") String userlogin){
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
                throw new ForbiddenException("Solo el creador puede añadir participantes");

            if(eventDAO.checkUserInEvent(id, user.getId()))
                throw new ForbiddenException("El usuario ya forma parte  del evento");

           if(eventDAO.eventIsFull(id))
              throw new ForbiddenException("Evento lleno");

            if (!eventDAO.JoinEvent(user.getId(), id))
                throw new NotFoundException("Couldn't add participant to event: " + id);



        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return user;


    }

    @Path("{id}/participant/del/{loginuser}")
    @DELETE
    public void deleteUserFromEvent(@PathParam("id") String id, @PathParam("loginuser") String userlogin){

        String userid = securityContext.getUserPrincipal().getName();
        EventDAO eventDAO = new EventDAOImpl();
        Event event = null;
        UserDAO userDAO = new UserDAOImpl();
        User user = null;

        try {

            event = eventDAO.getEventbyId(id);
            String creator = event.getCreator();
            System.out.println("Tu "+userid);
            System.out.println("Evento" + id);
            user = userDAO.getUserByLoginid(userlogin);
            String iduserTodelete = user.getId();



            System.out.println("User a borrar " + userlogin);

            if (event == null)
                throw new NotFoundException("Event with id = " + id + " not found");

            if(iduserTodelete.equals(userid))
                throw new ForbiddenException("No puedes eliminarte si eres el creador");

            if(!eventDAO.checkUserInEvent(id, userid))
                throw new ForbiddenException("You are not in the event");

            if (!userid.equals(creator))
                throw new ForbiddenException("Solo el creador puede borrar participantes");

            if (!eventDAO.LeaveEvent(iduserTodelete, id))
                throw new NotFoundException("Couldn't delete participant with id: " + id);



        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
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


    @Path("{id}/sharewithgroup")
    @POST
    @Produces(WalkaMediaType.WALKA_USER_COLLECTION)
    public UserCollection shareEventWithGroup(@PathParam("id") String eventid, @FormParam("groupid") String groupid){

        UserCollection peopleAdded;
        Event event = null;
        Group group = null;
        EventDAO eventDAO = new EventDAOImpl();
        GroupDAO groupDAO = new GroupDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();

        System.out.println(userid);

        try {




            System.out.println("1");

            if (eventDAO.getEventbyId(eventid) == null)
                throw new NotFoundException("Event with id = " + eventid + " not found");

            event = eventDAO.getEventbyId(eventid);
            String creator = event.getCreator();


            if (groupDAO.getGroupbyId(groupid) == null)
                throw new NotFoundException("Group with id = " + groupid + " doesn't exists");

            group = groupDAO.getGroupbyId(groupid);

            if (!userid.equals(creator))
                throw new ForbiddenException("Solo el creador puede compartir este evento");

            if(!groupDAO.checkUserInGroup(groupid,userid))
                throw new ForbiddenException("No formas parte de dicho grupo");

            if(eventDAO.eventIsFull(eventid))
                throw new ForbiddenException("Event is full");


            peopleAdded = groupDAO.addGroupMembersToEvent(groupid,eventid);


        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return peopleAdded;
    }

    @Path("/search")
    @GET
    @Produces(WalkaMediaType.WALKA_EVENT_COLLECTION)
    public EventCollection searchEvents(@QueryParam("keyword") String keyword){
        EventCollection eventCollection;
        EventDAO eventDAO = new EventDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();

        try {
            eventCollection = eventDAO.searchEvents(keyword,userid);
            eventCollection.setFromDate("Search: " + keyword);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;

    }

}
