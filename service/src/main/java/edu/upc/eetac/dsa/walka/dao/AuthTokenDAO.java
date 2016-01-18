package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.auth.UserInfo;
import edu.upc.eetac.dsa.walka.entity.AuthToken;

import java.sql.SQLException;

/**
 * Created by SergioGM on 05.12.15.
 */
public interface AuthTokenDAO {
    public UserInfo getUserByAuthToken(String token) throws SQLException;
    public AuthToken createAuthToken(String userid) throws SQLException;
    public void deleteToken(String userid) throws  SQLException;
}
