package edu.upc.eetac.dsa.walka.client.entity;

import java.util.List;

/**
 * Created by Marta_ on 29/12/2015.
 */
public class User {
    private List<Link> links;
    private String id;
    private String loginid;
    private String email;
    private String fullname;
    private String country;
    private String city;
    private String phonenumber;
    private String birthdate;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    private Boolean loginSuccesful;

    public void setLoginSuccesful(Boolean loginSuccesful) {
        this.loginSuccesful = loginSuccesful;
    }

    public Boolean getLoginSuccesful() {
        return loginSuccesful;
    }

}
