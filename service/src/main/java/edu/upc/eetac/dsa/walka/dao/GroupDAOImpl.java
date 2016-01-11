package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.db.Database;
import edu.upc.eetac.dsa.walka.entity.*;

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
    public Group updateGroup(String id, String name, String description) throws SQLException {
        Group group = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.UPDATE_GROUP);

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, id);
            int rows = stmt.executeUpdate();
            if (rows == 1)
                group = getGroupbyId(id);

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return group;
    }

    @Override
    public boolean deleteGroup(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        System.out.println("deleteGroup");
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.DELETE_GROUP);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean checkUserInGroup(String idgroup, String iduser) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        System.out.println(idgroup);
        System.out.println(iduser);

        try {

            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.CHECK_USER_IN_GROUP);
            stmt.setString(1, iduser);
            stmt.setString(2, idgroup);

            ResultSet rs= stmt.executeQuery();

            return  (rs.next());



        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean checkUserInInvitation(String idgroup, String iduser) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {

            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.CHECK_IF_USER_IS_INVITED_TO_GROUP);
            stmt.setString(1, idgroup);
            stmt.setString(2, iduser);

            ResultSet rs= stmt.executeQuery();

            return  (rs.next());

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public InvitationCollection checkInvitations(String userid) throws SQLException {
        InvitationCollection invitationCollection = new InvitationCollection();
        Invitation invitation = null;
        Group group = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        UserCollectionDAO userDAO = new UserCollectionDAOImpl();
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.CHECK_INVITATIONS);
            stmt.setString(1, userid);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                invitation = new Invitation();
                group = new Group();
                group = getGroupbyId(rs.getString("groupid"));
                invitation.setGroupInvitator(group);
                invitation.setUserInvitedId(rs.getString("userInvited"));
                invitationCollection.getInvitations().add(invitation);

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return invitationCollection;
    }

    @Override
    public boolean addUserToGroup(String idgroup, String iduser) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.ADD_USER_TO_GROUP);
            stmt.setString(1, idgroup);
            stmt.setString(2, iduser);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean deleteUserFromGroup(String idgroup, String iduser) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.DELETE_USER_FROM_GROUP);
            stmt.setString(1, idgroup);
            stmt.setString(2, iduser);


            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean inviteUserToGroup(String idgroup, String iduser) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.INVITE_USERID_TO_GROUP);
            stmt.setString(1, idgroup);
            stmt.setString(2, iduser);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean deleteUserFromInvitations(String idgroup, String userInvited) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.DELETE_USER_FROM_PENDING_INVITATIONS);
            stmt.setString(1, idgroup);
            stmt.setString(2, userInvited);


            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public UserCollection addGroupMembersToEvent(String idgroup, String idevent) throws SQLException {
        UserCollection groupMembers = new UserCollection();
        UserCollection peopleAdded = new UserCollection();
        EventDAO eventDAO = new EventDAOImpl();


        Connection connection = null;
        PreparedStatement stmt = null;


        try {
            //Obtengo participantes
            connection = Database.getConnection();
            stmt = connection.prepareStatement(UserCollectionDAOQuery.GET_USERS_BY_GROUP_ID);
            stmt.setString(1, idgroup);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setLoginid(rs.getString("loginid"));
                user.setEmail(rs.getString("email"));
                user.setFullname(rs.getString("fullname"));

                groupMembers.getUsers().add(user);

            }

            //Añado si no estan ya en el evento

            for (User member: groupMembers.getUsers()){
                System.out.println("Miembros: " + member.getFullname() + "ID: " + member.getId());

                if (!eventDAO.checkUserInEvent(idevent, member.getId())){

                    System.out.println("Añado: " + member.getFullname() + "con ID: " + member.getId());
                    eventDAO.JoinEvent(member.getId(), idevent);
                    peopleAdded.getUsers().add(member);
                }
            }


        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return peopleAdded;
    }

    @Override
    public boolean groupIsFull(String idgroup) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        int NParticipants = 0;
        System.out.println("Checks if it's full");

        try {

            connection = Database.getConnection();

            stmt = connection.prepareStatement(GroupDAOQuery.GET_NUMBER_PARTICIPANTS_GROUP);
            stmt.setString(1, idgroup);

            ResultSet rs= stmt.executeQuery();
            rs.next();
            System.out.println(rs.getInt("participants"));
            NParticipants = rs.getInt(1);
            //Si el numero de personas llega al limite, no se permite añadir
            return (NParticipants>=GroupDAOQuery.MAX_NUMBER_PEOPLE_GROUP);



        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}
