package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.dao.GroupDAO;
import edu.upc.eetac.dsa.walka.dao.GroupDAOImpl;
import edu.upc.eetac.dsa.walka.dao.UserDAO;
import edu.upc.eetac.dsa.walka.dao.UserDAOImpl;
import edu.upc.eetac.dsa.walka.entity.AuthToken;
import edu.upc.eetac.dsa.walka.entity.Group;
import edu.upc.eetac.dsa.walka.entity.InvitationCollection;
import edu.upc.eetac.dsa.walka.entity.User;

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


            //Introduzco en user_groups
            groupDAO.addUserToGroup(group.getId(), securityContext.getUserPrincipal().getName());



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

        String userid = securityContext.getUserPrincipal().getName();
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


            if(!groupDAO.checkUserInGroup(id, userid))
                throw new ForbiddenException("You are not in the group");


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


    @Path("/{id}")
    @PUT
    @Consumes(WalkaMediaType.WALKA_GROUP)
    @Produces(WalkaMediaType.WALKA_GROUP)
    public Group updateGroup(@PathParam("id") String id, Group group) {
        GroupDAO groupDAO = new GroupDAOImpl();

        if (group == null)
            throw new BadRequestException("Entity is null");
        if (!id.equals(group.getId()))
            throw new BadRequestException("Path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();



        try {

            Group checkG = groupDAO.getGroupbyId(group.getId());

            if (checkG == null)
                throw new NotFoundException("Group with id = " + id + "not found");

            if(!groupDAO.checkUserInGroup(id, userid))

                throw new ForbiddenException("You are not in the group");
            System.out.println(group.getName());
            group = groupDAO.updateGroup(group.getId(),group.getName(),group.getDescription());

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return group;
    }

    @Path("/{id}")
    @DELETE
    public void deleteGroup(@PathParam("id") String id) {
        System.out.println(id);
        String userid = securityContext.getUserPrincipal().getName();
        GroupDAO groupDAO = new GroupDAOImpl();

        Group group = null;

        System.out.println(userid);


        try {

            group = groupDAO.getGroupbyId(id);
            String creator = group.getCreator();
            System.out.println(creator);


            if (group == null)
                throw new NotFoundException("Group with id = " + id + "not found");

            if (!userid.equals(creator))
                throw new ForbiddenException("You are not the creator");

            if (!groupDAO.deleteGroup(group.getId()))
                throw new NotFoundException("Couldn't delete group:  " + id);

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Path("/{id}/invite")
    @POST
    @Produces(WalkaMediaType.WALKA_USER)
    public User inviteUserToGroup(@PathParam("id") String id, @FormParam("loginuser") String userlogin){

        String userid = securityContext.getUserPrincipal().getName();
        GroupDAO groupDAO = new GroupDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        Group group = null;
        User user = null;
        System.out.println(id);
        System.out.println(userid);

        try {
            if(groupDAO.getGroupbyId(id) == null)
                throw new NotFoundException("Group with id = " + id + " not found");
            group = groupDAO.getGroupbyId(id);
            String creator = group.getCreator();
            System.out.println(creator);

            if (userDAO.getUserByLoginid(userlogin) == null)
                throw new NotFoundException("User with login = " + userlogin + " doesn't exists");

            user = userDAO.getUserByLoginid(userlogin);
            System.out.println(user.getLoginid());


            if (!userid.equals(creator))
                throw new ForbiddenException("Only the creator can invite people to group");

            if(groupDAO.groupIsFull(id))
                throw new ForbiddenException("Group is full");

            if(groupDAO.checkUserInGroup(id, user.getId()))
                throw new ForbiddenException("The user is already in the group");

            if(groupDAO.checkUserInInvitation(id, user.getId()))
                throw new ForbiddenException("The user is already invited. Waiting for confirmation...");
            System.out.println(id);
            System.out.println(user.getId());
            if(!groupDAO.inviteUserToGroup(id, user.getId()))
                throw new NotFoundException("Couldn't invite to group: " + id);



        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return user;


    }

    @Path("/invitations")
    @GET
    @Produces(WalkaMediaType.WALKA_INVITATION_COLLECTION)
    public InvitationCollection checkInvitations(){
        InvitationCollection invitationCollection;
        GroupDAO groupDAO = new GroupDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();

        try {
            invitationCollection = groupDAO.checkInvitations(userid);

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return invitationCollection;
    }

    @Path("/{id}/invitations/accept")
    @POST
    @Produces(WalkaMediaType.WALKA_GROUP)
    public Group acceptInvitation(@PathParam("id") String id){

        Group group;
        GroupDAO groupDAO= new GroupDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();
        UserDAO userDAO = new UserDAOImpl();
        User user = null;

        System.out.println(id);
        System.out.println(userid);

        try {

            if (groupDAO.getGroupbyId(id) == null)
                throw new NotFoundException("Group with id = " + id + " not found");

            group = groupDAO.getGroupbyId(id);
            String creator = group.getCreator();
            System.out.println("Obtengo bien el grupo: "+ group.getId());
            System.out.println(creator);

            if (!groupDAO.checkUserInInvitation(id,userid))
                throw new ForbiddenException("You were no invited to this group");

            System.out.println("1");

            if(groupDAO.checkUserInGroup(id,userid))
                throw new ForbiddenException("You are already in the group");

            System.out.println("2");

            if(groupDAO.groupIsFull(id))
                throw new ForbiddenException("Group is full");

            System.out.println("3");

            //Si lo puedo añadir, lo quito de invitaciones

            if (groupDAO.addUserToGroup(id,userid))
                groupDAO.deleteUserFromInvitations(id,userid);
            System.out.println("4");



        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return group;
    }

    @Path("/{id}/invitations/refuse")
    @POST
    public void refuseInvitation(@PathParam("id") String id){

        Group group;
        GroupDAO groupDAO= new GroupDAOImpl();
        String userid = securityContext.getUserPrincipal().getName();
        UserDAO userDAO = new UserDAOImpl();
        User user = null;

        System.out.println(id);
        System.out.println(userid);

        try {

            if (groupDAO.getGroupbyId(id) == null)
                throw new NotFoundException("Group with id = " + id + " not found");

            group = groupDAO.getGroupbyId(id);
            String creator = group.getCreator();
            System.out.println("Obtengo bien el grupo: "+ group.getId());
            System.out.println(creator);

            if (!groupDAO.checkUserInInvitation(id,userid))
                throw new ForbiddenException("You were no invited to this group");

            System.out.println("1");

            if(groupDAO.checkUserInGroup(id,userid))
                throw new ForbiddenException("You are already in the group");

            System.out.println("2");

            if(groupDAO.groupIsFull(id))
                throw new ForbiddenException("Group is full");

            System.out.println("3");

            //Si lo puedo añadir, lo quito de invitaciones

            if (!groupDAO.deleteUserFromInvitations(id,userid))
                throw new InternalServerErrorException("Couldn't do the operation");

            System.out.println("4");



        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }


    }




}
