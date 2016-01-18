package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.entity.User;

import java.sql.SQLException;

/**
 * Created by SergioGM on 05.12.15.
 */
public interface UserDAO {
    public User createUser(String loginid, String password, String email, String fullname, String country, String city, String phonenumber, String birthdate) throws SQLException, UserAlreadyExistsException;

    public User updateProfile(String id, String email, String fullname, String country, String city, String phonenumber, String birthdate) throws SQLException;

    public User getUserById(String id) throws SQLException;

    public User getUserByLoginid(String loginid) throws SQLException;

    public boolean deleteUser(String id) throws SQLException;

    public boolean checkPassword(String id, String password) throws SQLException;

    public String getIdfromMail(String email)throws SQLException;
}
