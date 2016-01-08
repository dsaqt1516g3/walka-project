package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.walka.dao.UserAlreadyExistsException;
import edu.upc.eetac.dsa.walka.dao.UserDAO;
import edu.upc.eetac.dsa.walka.dao.UserDAOImpl;
import edu.upc.eetac.dsa.walka.entity.AuthToken;
import edu.upc.eetac.dsa.walka.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by SergioGM on 12.12.15.
 */

@Path("users")
public class UserResource {
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(WalkaMediaType.WALKA_AUTH_TOKEN)
    public Response registerUser(@FormParam("loginid") String loginid, @FormParam("password") String password, @FormParam("email") String email, @FormParam("fullname") String fullname, @FormParam("country") String country,@FormParam("city") String city, @FormParam("phonenumber") String phonenumber, @FormParam("birthdate") String birthdate, @Context UriInfo uriInfo) throws URISyntaxException {
        if(loginid == null || password == null || email == null || fullname == null)
            throw new BadRequestException("All parameters are mandatory");
        UserDAO userDAO = new UserDAOImpl();
        User user = null;
        AuthToken authenticationToken = null;
        try{
            user = userDAO.createUser(loginid, password, email, fullname, country, city, phonenumber, birthdate);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(user.getId());
        }catch (UserAlreadyExistsException e){
            throw new WebApplicationException("Loginid already exists", Response.Status.CONFLICT);
        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
        return Response.created(uri).type(WalkaMediaType.WALKA_AUTH_TOKEN).entity(authenticationToken).build();
    }


    @Path("/{id}")
    @GET
    @Produces(WalkaMediaType.WALKA_USER)
    public User getUser(@PathParam("id") String id) {
        User user = null;
        try {
            user = (new UserDAOImpl()).getUserById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if(user == null)
            throw new NotFoundException("User with id = "+id+" doesn't exist");
        return user;
    }

    @Path("/{id}")
    @PUT
    @Consumes(WalkaMediaType.WALKA_USER)
    @Produces(WalkaMediaType.WALKA_USER)
    public User updateUser(@PathParam("id") String id, User user) {
        System.out.println(user.getId());
        System.out.println(user.getCity());
        System.out.println(user.getCountry());
        System.out.println(user.getPhonenumber());
        System.out.println(user.getBirthdate());


        if(user == null)
            throw new BadRequestException("Entity is null");
        if(!id.equals(user.getId()))
            throw new BadRequestException("Path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("Operation not allowed");

        UserDAO userDAO = new UserDAOImpl();
        try {
            user = userDAO.updateProfile(userid, user.getEmail(), user.getFullname(), user.getCountry(), user.getCity(), user.getPhonenumber(), user.getBirthdate());
            if(user == null)
                throw new NotFoundException("User with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return user;
    }

    @Path("/{id}")
    @DELETE
    public void deleteUser(@PathParam("id") String id){
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        UserDAO userDAO = new UserDAOImpl();
        try {
            if(!userDAO.deleteUser(id))
                throw new NotFoundException("User with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}




