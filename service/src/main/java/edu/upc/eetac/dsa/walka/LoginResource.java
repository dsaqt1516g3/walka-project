package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.dao.AuthTokenDAO;
import edu.upc.eetac.dsa.walka.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.walka.dao.UserDAO;
import edu.upc.eetac.dsa.walka.dao.UserDAOImpl;
import edu.upc.eetac.dsa.walka.entity.AuthToken;
import edu.upc.eetac.dsa.walka.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.sql.SQLException;

/**
 * Created by SergioGM on 17.12.15.
 */
@Path("login")
public class LoginResource {
    @Context
    SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(WalkaMediaType.WALKA_AUTH_TOKEN)
    public AuthToken login(@FormParam("login") String loginid, @FormParam("password") String password) {
        if(loginid == null || password == null)
            throw new BadRequestException("All parameters are mandatory");

        User user = null;
        AuthToken authToken = null;
        try{
            UserDAO userDAO = new UserDAOImpl();
            user = userDAO.getUserByLoginid(loginid);
            if(user == null)
                throw new BadRequestException("Loginid " + loginid + " not found.");
            if(!userDAO.checkPassword(user.getId(), password))
                throw new BadRequestException("Incorrect password");

            AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl();
            authTokenDAO.deleteToken(user.getId());
            authToken = authTokenDAO.createAuthToken(user.getId());
        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
        return authToken;
    }

    @DELETE
    public void logout(){
        String userid = securityContext.getUserPrincipal().getName();
        AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl();
        try {
            authTokenDAO.deleteToken(userid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
