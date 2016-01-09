package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.db.Database;
import edu.upc.eetac.dsa.walka.entity.Group;
import edu.upc.eetac.dsa.walka.entity.User;
import edu.upc.eetac.dsa.walka.entity.UserCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SergioGM on 09.01.16.
 */
public class GroupDAOImpl implements GroupDAO {

    @Override
    public Group createGroup(String creator, String name, String description) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;

        System.out.println(name);
        System.out.println(description);
        System.out.println(creator);
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(GroupDAOQuery.CREATE_GROUP);
            stmt.setString(1, id);
            stmt.setString(2, creator);
            stmt.setString(3, name);
            stmt.setString(4, description);
            stmt.executeUpdate();


        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getGroupbyId(id);
    }

    @Override
    public Group getGroupbyId(String id) throws SQLException {
        Group group = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        UserCollectionDAO userDAO = new UserCollectionDAOImpl();
        UserDAO userD = new UserDAOImpl();

        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.GET_GROUP_BY_ID);
            stmt.setString(1, id);


            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                group = new Group();
                group.setId(rs.getString("id"));
                group.setCreator(rs.getString("idcreator"));
                User user = userD.getUserById(group.getCreator());
                group.setCreatorName(user.getFullname());
                group.setName(rs.getString("name"));
                group.setDescription(rs.getString("description"));
                /**Obtengo participantes de otra clase*/
                UserCollection components = userDAO.getUsersByGroupId(group.getId());
                group.setComponents(components);
                group.setLastModified(rs.getTimestamp("last_modified").getTime());
                group.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());


            }


        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return group;
    }

    @Override
    public Group updateGroup(String id, String creator, String name, String description) throws SQLException {
        return null;
    }

    @Override
    public boolean deleteGroup(String id) throws SQLException {
        return false;
    }
}
