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
import edu.upc.eetac.dsa.walka.client.entity.Event;
import edu.upc.eetac.dsa.walka.client.entity.Link;
import edu.upc.eetac.dsa.walka.client.entity.Root;
import edu.upc.eetac.dsa.walka.client.entity.User;

/**
 * Created by Marta_ on 29/12/2015.
 */
public class WalkaClient {

    private final static String BASE_URI = "http://147.83.7.204:8087/walka";
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

    public User login(String userid, String password){
        User user = new User();
        user.setLoginid(userid);
        //user.setPassword(password);

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

            //user.setPassword(pass);
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

    //Eventos de un mes
    public String getEvents(String uri, String date) throws WalkaClientException {
        Log.d(TAG, "entro en el Walka Client");

        if(uri==null){
            uri = getLink(authToken.getLinks(), "fill-calendar").getUri().toString();
            uri = uri + ("/month?monthdate="+date );
            Log.d(TAG, "Uri events: " +uri);
        }
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).get();
        Log.d(TAG, "response"+ response);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        }
        else {
            throw new WalkaClientException(response.readEntity(String.class));
        }
    }
    //Eventos de una semana
    public String getWeekEvents(String uri, String date) throws WalkaClientException {
        Log.d(TAG, "entro en el Walka Client");

        if(uri==null){
            uri = getLink(authToken.getLinks(), "fill-calendar").getUri().toString();
            uri = uri + ("/week?weekdate="+date );
            Log.d(TAG, "Uri stings: " +uri);
        }
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).get();
        Log.d(TAG, "response"+ response);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        }
        else {
            throw new WalkaClientException(response.readEntity(String.class));
        }
    }

    //Detalles de un evento
    public String getEvent(String uri, String id) throws  WalkaClientException {
        Log.d(TAG, "entro en el getEvent del walka client");
        if(uri==null){
            uri = BASE_URI + ("/events/") + id;
            Log.d(TAG, "Uri stings: " +uri);
        }
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).get();
        Log.d(TAG, "response"+ response);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        }
        else {
            throw new WalkaClientException(response.readEntity(String.class));
        }
    }

    //Obtener perfil de un usuario
    public String getUser(String uri) throws  WalkaClientException {
        Log.d(TAG, "entro en el getUser del walka client");
        if(uri==null){
            uri = getLink(authToken.getLinks(), "user-profile").getUri().toString();
        }
        WebTarget target = client.target(uri);
        Response response = target.request().get();
        Log.d(TAG, "response"+ response);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(String.class);
        }
        else {
            throw new WalkaClientException(response.readEntity(String.class));
        }
    }
    //Logout
    public Boolean LogOut (String uri) throws WalkaClientException{
        Boolean ok = false;
        if(uri==null){
            uri = getLink(authToken.getLinks(), "logout").getUri().toString();
            Log.d(TAG, "Uri stings: " +uri);
        }
        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).delete();
        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            ok = true;
            Log.d(TAG, "bool" +ok.toString());
            return ok;
        }
        else {
            throw new WalkaClientException(response.readEntity(String.class));
        }
    }

    //BorrarEvento

    public Boolean Eliminar (String id) throws WalkaClientException{
        Boolean ok = false;
        String uri = null;
            uri = getLink(authToken.getLinks(), "create-event").getUri().toString();
            uri = uri + ("/"+id);
            Log.d(TAG, "Uri delete: " +uri);

        WebTarget target = client.target(uri);
        Response response = target.request().header("X-Auth-Token", authToken.getToken()).delete();
        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            ok = true;
            Log.d(TAG, "bool" +ok.toString());
            return ok;
        }
        else {
            throw new WalkaClientException(response.readEntity(String.class));
        }
    }

    //Crear evento
    public Boolean newEvent(Event event) throws WalkaClientException {
        Log.d(TAG, "entro en el Walka Client");
        Boolean ok = false;
        String uri;
        uri = getLink(authToken.getLinks(), "create-event").getUri().toString();
        WebTarget target = client.target(uri);
        Form form = new Form();
        form.param("title", event.getTitle());
        form.param("location", event.getLocation());
        form.param("notes", event.getNotes());
        form.param("start", event.getStart());
        form.param("end", event.getEnd());
        form.param("tag", event.getTag());

        response = target.request().header("X-Auth-Token", authToken.getToken()).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
        Log.d(TAG, "response: " + response);

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            ok = true;
            Log.d(TAG, "bool" +ok.toString());
            return ok;
        }
        else {
            throw new WalkaClientException(response.readEntity(String.class));
        }
    }

    //Editar usuario
    public Boolean EditUser(User user){
        String uri;
        Boolean ok;
        uri = getLink(authToken.getLinks(), "user-profile").getUri().toString();
        WebTarget target = client.target(uri);
        Form form = new Form();
        form.param("id", user.getId());
        Log.d(TAG, "id" + user.getId());
        form.param("loginid", user.getLoginid());
        Log.d(TAG, "loginid" + user.getLoginid());
        form.param("email", user.getEmail());
        Log.d(TAG, "email" + user.getEmail());
        form.param("fullname", user.getFullname());
        Log.d(TAG, "fullname" + user.getFullname());
        form.param("country", user.getCountry());
        Log.d(TAG, "country" + user.getCountry());
        form.param("city", user.getCity());
        form.param("phonenumber", user.getPhonenumber());
        form.param("birthdate", user.getBirthdate());

        Log.d(TAG, form.toString());
        response = target.request().header("X-Auth-Token", authToken.getToken()).header("Content-Type", WalkaMediaType.WALKA_USER).put(Entity.entity((new Gson().toJson(form)), WalkaMediaType.WALKA_USER), Response.class);

        Log.d(TAG, "response: "+ response);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return true;
        }
        else {
            return false;
        }
    }
}
