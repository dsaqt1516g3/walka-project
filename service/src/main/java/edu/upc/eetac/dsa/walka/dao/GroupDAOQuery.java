package edu.upc.eetac.dsa.walka.dao;

/**
 * Created by SergioGM on 09.01.16.
 */
public class GroupDAOQuery {

    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_GROUP = "insert into groups (id, loginid, password, email, fullname, country, city, phonenumber, birthdate) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?, ?, ?, ?);";
    public final static String UPDATE_GROUP ="";
    public final static String DELETE_GROUP ="";
    public final static String GET_GROUP_BY_ID="";
    public final static String GET_USERS_GROUPID ="";
    public final static String GET_GROUPS_USERID ="";
    public final static String INVITE_USERID_TO_GROUP="";
    public final static String ADD_USER_TO_GROUP =""  ;
    public final static String DELETE_USER_FROM_GROUP="";
    public final static String DELETE_USER_FROM_PENDING_INV="";
    public final static String LEAVE_GROUP="";


}
