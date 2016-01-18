package edu.upc.eetac.dsa.walka.client;

import android.net.ConnectivityManager;
import android.util.Log;

import com.google.gson.Gson;

import org.glassfish.jersey.client.ClientConfig;

import java.net.HttpURLConnection;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;


import edu.upc.eetac.dsa.walka.R;
import edu.upc.eetac.dsa.walka.client.entity.AuthToken;
import edu.upc.eetac.dsa.walka.client.entity.Link;
import edu.upc.eetac.dsa.walka.client.entity.Root;
import edu.upc.eetac.dsa.walka.client.entity.User;

/**
 * Created by Marta_ on 29/12/2015.
 */
public class WalkaClient {

    private final static String BASE_URI = "http://192.168.1.129:8080/walka";
    private static WalkaClient instance;
    private Root root;
    private ClientConfig clientConfig = null;
    private Client client = null;
    private AuthToken authToken = null;
    private final static String TAG = WalkaClient.class.toString();
    protected Response response;

    private WalkaClient() {
        clientConfig = new ClientConfig();
        client = ClientBuilder.newClient(clientConfig);
        loadRoot();
    }

    public static WalkaClient getInstance() {
        if (instance == null)
            instance = new WalkaClient();
        return instance;
    }

    private void loadRoot() {
        WebTarget target = client.target(BASE_URI);
        Response response = target.request().get();

        String json = response.readEntity(String.class);
        root = (new Gson()).fromJson(json, Root.class);
    }

    public Root getRoot() {
        return root;
    }

    public final static Link getLink(List<Link> links, String rel) {
        for (Link link : links) {
            if (link.getRels().contains(rel)) {
                return link;
            }
        }
        return null;
    }

    //Login

    public User login(String userid, String password) {
        User user = new User();
        user.setLoginid(userid);
        user.setPassword(password);

        String loginUri = getLink(root.getLinks(), "login").getUri().toString();
        WebTarget target = client.target(loginUri);
        Form form = new Form();
        form.param("login", userid);
        form.param("password", password);
        response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);

        String reason = response.getStatusInfo().getReasonPhrase();
        int code = response.getStatus();

        if (code == 200) {
            String json = response.readEntity(String.class);
            authToken = (new Gson()).fromJson(json, AuthToken.class);
            user.setLoginSuccesful(true);
            return user;
        }
        else{
            user.setLoginSuccesful(false);
            Log.d(TAG, "Error: " + code);
            return user;
        }

    }

    //Register
    public User register(String userid, String name, String email, String pass){
        Log.d(TAG, "entra en el register" );
        User user = new User();
        String loginUri = getLink(root.getLinks(), "create-user").getUri().toString();
        WebTarget target = client.target(loginUri);
        Log.d(TAG, "uri: " + loginUri);
        Form form = new Form();
        form.param("loginid", userid);
        form.param("password", pass);
        form.param("email", email);
        form.param("fullname", name);
        Log.d(TAG, form.toString());
        response = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
        Log.d(TAG, "response: "+ response);

        String reason = response.getStatusInfo().getReasonPhrase();
        int code = response.getStatus();

        if (code == 201) {
            String json = response.readEntity(String.class);
            authToken = (new Gson()).fromJson(json, AuthToken.class);

            user.setPassword(pass);
            user.setEmail(email);
            user.setLoginid(userid);
            user.setFullname(name);
            user.setLoginSuccesful(true);
            Log.d(TAG, "todo chachi");

            return user;
        }
        else if(code == 409){
            user.setLoginSuccesful(false);
            user.setLoginid(null);
            return user;
        }
        else {
            user.setLoginSuccesful(false);
            Log.d(TAG, "Error: " + code);
            return user;
        }
    }

}
