package edu.upc.eetac.dsa.walka;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Created by SergioGM on 05.12.15.
 */
public class WalkaResourceConfig extends ResourceConfig {

    public WalkaResourceConfig() {

        packages("edu.upc.eetac.dsa.walka");
        packages("edu.upc.eetac.dsa.beeter.auth");
        register(RolesAllowedDynamicFeature.class);
    }


}
