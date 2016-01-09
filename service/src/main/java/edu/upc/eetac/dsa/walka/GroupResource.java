package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.dao.GroupDAO;
import edu.upc.eetac.dsa.walka.dao.GroupDAOImpl;
import edu.upc.eetac.dsa.walka.entity.AuthToken;
import edu.upc.eetac.dsa.walka.entity.Group;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by SergioGM on 09.01.16.
 */
@Path("groups")
public class GroupResource {

    @Context
    private SecurityContext securityContext;
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(WalkaMediaType.WALKA_GROUP)
    public Response createGroup(@FormParam("name") String name, @FormParam("description") String description, @Context UriInfo uriInfo) throws URISyntaxException {
        if (name == null )
            throw new BadRequestException("Name is mandatory");
        GroupDAO groupDAO = new GroupDAOImpl();

        Group group = null;
        AuthToken authToken = null;
        System.out.println(name);
        System.out.println(description);
        System.out.println(securityContext.getUserPrincipal().getName());

        try {
            group = groupDAO.createGroup(securityContext.getUserPrincipal().getName(), name, description);
            String id = group.getId();
            System.out.println(id);


            //Introduzco en user_events
            //eventDAO.JoinEvent(securityContext.getUserPrincipal().getName(), event.getId());


        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + group.getId());
        return Response.created(uri).type(WalkaMediaType.WALKA_EVENT).entity(group).build();


    }

    @Path("/{id}")
    @GET
    @Produces(WalkaMediaType.WALKA_GROUP)
    public Response getGroup(@PathParam("id") String id, @Context Request request) {
        // Create cache-control

        //String userid = securityContext.getUserPrincipal().getName();
        CacheControl cacheControl = new CacheControl();
        Group group = null;
        GroupDAO groupDAO  = new GroupDAOImpl();

        try {
                System.out.println("1");
            //Si el id de grupo no existe
            group = groupDAO.getGroupbyId(id);

            System.out.println("2");


            if (group == null)
                throw new NotFoundException("Group with id = " + id + "not found");

            //Si no pertenece al grupo, no puede obtenerlo
            //-----------------------------------------------------------------------------------------------------
            //if(!eventDAO.checkUserInEvent(id,userid))
                //throw new ForbiddenException("You are not in the event");
            //-----------------------------------------------------------------------------------------------------

            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(group.getLastModified()));

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
            rb = Response.ok(group).cacheControl(cacheControl).tag(eTag);
            return rb.build();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }





}
