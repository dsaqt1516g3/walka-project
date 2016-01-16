package edu.upc.eetac.dsa.walka;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Created by SergioGM on 05.12.15.
 */
public class WalkaResourceConfig extends ResourceConfig {

    public WalkaResourceConfig() {

        packages("edu.upc.eetac.dsa.walka");
        packages("edu.upc.eetac.dsa.walka.auth");
        packages("edu.upc.eetac.dsa.walka.cors");
        register(RolesAllowedDynamicFeature.class);
        register(DeclarativeLinkingFeature.class);
        register(JacksonFeature.class);
    }


}
