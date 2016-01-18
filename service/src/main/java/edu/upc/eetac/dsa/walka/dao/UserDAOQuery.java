package edu.upc.eetac.dsa.walka.dao;

/**
 * Created by SergioGM on 05.12.15.
 */
public interface UserDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_USER = "insert into users (id, loginid, password, email, fullname, country, city, phonenumber, birthdate) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?, ?, ?, ?, ?);";
    public final static String UPDATE_USER = "update users set email=?, fullname=?, country=?, city=?, phonenumber=?, birthdate=? where id=unhex(?)";
    public final static String ASSIGN_ROLE_REGISTERED = "insert into user_roles (userid, role) values (UNHEX(?), 'registered')";
    public final static String GET_USER_BY_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.country, u.city, u.phonenumber, u.birthdate from users u where id=unhex(?)";
    public final static String GET_USER_BY_USERNAME = "select hex(u.id) as id, u.loginid, u.email, u.fullname, u.country, u.city, u.phonenumber, u.birthdate from users u where u.loginid=?";
    public final static String DELETE_USER = "delete from users where id=unhex(?)";
    public final static String GET_PASSWORD =  "select hex(password) as password from users where id=unhex(?)";
    public final static String GET_MAIL_FROM_ID = "select hex(id) from users where email=?";
}
