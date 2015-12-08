package edu.upc.eetac.dsa.walka.dao;

/**
 * Created by SergioGM on 05.12.15.
 */
public class AuthTokenDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_TOKEN = "insert into auth_tokens (userid, token) values (UNHEX(?), UNHEX(?))";
    public final static String GET_USER_BY_TOKEN = "select hex(u.id) as id from users u, auth_tokens t where t.token=unhex(?) and u.id=t.userid";
    public final static String GET_ROLES_OF_USER = "select hex(userid), role from user_roles where userid=unhex(?)";
    public final static String DELETE_TOKEN = "delete from auth_tokens where userid = unhex(?)";
}
