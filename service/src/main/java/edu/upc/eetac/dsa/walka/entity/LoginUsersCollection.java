package edu.upc.eetac.dsa.walka.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SergioGM on 18.12.15.
 */
public class LoginUsersCollection {
    private List<String> loginids = new ArrayList<>();



    public List<String> getLoginids() {
        return loginids;
    }

    public void setLoginids(List<String> loginids) {
        this.loginids = loginids;
    }
}
