package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.db.Database;
import edu.upc.eetac.dsa.walka.entity.User;
import edu.upc.eetac.dsa.walka.entity.UserCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SergioGM on 07.12.15.
 */
public class UserCollectionDAOImpl implements UserCollectionDAO {

    @Override
    public UserCollection getParticipantsByEventId(String eventid) throws SQLException {

        UserCollection userCollection = new UserCollection();

        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(UserCollectionDAOQuery.GET_PARTICIPANTS_BY_EVENT_ID);
            stmt.setString(1, eventid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setLoginid(rs.getString("loginid"));
                user.setEmail(rs.getString("email"));
                user.setFullname(rs.getString("fullname"));

                userCollection.getUsers().add(user);

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return userCollection;

    }
}
