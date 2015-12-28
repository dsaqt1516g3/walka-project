package edu.upc.eetac.dsa.walka;

import edu.upc.eetac.dsa.walka.entity.WalkaRootAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by SergioGM on 28.12.15.
 */
@Path("/")
public class WalkaRootAPIResource {
    @GET
    @Produces(WalkaMediaType.WALKA_ROOT)
    public WalkaRootAPI getRootAPI(){
        WalkaRootAPI walkaRootAPI = new WalkaRootAPI();

        return walkaRootAPI;
    }
}
