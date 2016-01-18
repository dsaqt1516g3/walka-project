package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.entity.WalkaRootAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by SergioGM on 28.12.15.
 */
@Path("/")
public class WalkaRootAPIResource {
    @Context
    private SecurityContext securityContext;

    private String userid;
    @GET
    @Produces(WalkaMediaType.WALKA_ROOT)
    public WalkaRootAPI getRootAPI(){
        if(securityContext.getUserPrincipal()!=null)
            userid = securityContext.getUserPrincipal().getName();
        WalkaRootAPI walkaRootAPI = new WalkaRootAPI();

        return walkaRootAPI;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
