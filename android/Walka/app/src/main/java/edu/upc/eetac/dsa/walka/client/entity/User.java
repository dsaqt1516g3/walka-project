package edu.upc.eetac.dsa.walka.client.entity;

import java.util.List;

import javax.ws.rs.core.*;

/**
 * Created by Marta_ on 29/12/2015.
 */
public class User {
    private String id;
    private String loginid;
    private String password;
    private String email;
    private String fullname;
    private Boolean loginSuccesful;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setLoginSuccesful(Boolean loginSuccesful) {
        this.loginSuccesful = loginSuccesful;
    }

    public Boolean getLoginSuccesful() {
        return loginSuccesful;
    }

}
