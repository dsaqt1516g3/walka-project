package edu.upc.eetac.dsa.walka.client.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Marta_ on 17/01/2016.
 */
public class UserCollection {
    private final static String TAG = UserCollection.class.toString();
    private List<Link> links;
    private List<User> users = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
