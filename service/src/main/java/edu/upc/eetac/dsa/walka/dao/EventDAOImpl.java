package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.db.Database;
import edu.upc.eetac.dsa.walka.entity.Event;
import edu.upc.eetac.dsa.walka.entity.EventCollection;
import edu.upc.eetac.dsa.walka.entity.UserCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SergioGM on 07.12.15.
 */
public class EventDAOImpl implements EventDAO {
    @Override
    public Event createEvent(String userid, String title, String location, String notes, long startDate, long endDate) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(EventDAOQuery.CREATE_EVENT);
            stmt.setString(1, id);
            stmt.setString(2, title);
            stmt.setString(3, userid);
            stmt.setString(4, location);
            stmt.setString(5, notes);
            stmt.setLong(6, startDate);
            stmt.setLong(7, endDate);
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
        return getEventbyId(id);
    }

    @Override
    public Event getEventbyId(String id) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        UserCollectionDAO userDAO = new UserCollectionDAOImpl();

        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.GET_EVENT_BY_ID);
            stmt.setString(1, id);



            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event();
                event.setId(rs.getString("id"));
                event.setTitle(rs.getString("title"));
                event.setCreator(rs.getString("creator"));
                event.setLocation(rs.getString("location"));
                /**Obtengo participantes de otra clase*/
                UserCollection participants = userDAO.getParticipantsByEventId(id);
                event.setParticipants(participants);
                event.setStartdate(rs.getLong("startdate"));
                event.setEnddate(rs.getLong("enddate"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return event;
    }

    @Override
    public EventCollection getEventsMonth() {
        return null;
    }


    @Override
    public EventCollection getEventsDay() {
        return null;
    }

    @Override
    public boolean JoinEvent(String userid, String eventid) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.JOIN_EVENT);
            stmt.setString(1, eventid);
            stmt.setString(2, userid);


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
    public boolean LeaveEvent(String userid, String eventid) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.LEAVE_EVENT);
            stmt.setString(1, eventid);
            stmt.setString(2, userid);


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
    public EventCollection getEventsFromTo(long fromData, long toData) throws SQLException {
        return null;
    }

    @Override
    public Event updateEvent(String id, String title, String location, String notes, long startDate, long endDate) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.UPDATE_EVENT);

            stmt.setString(1, title);
            stmt.setString(2, location);
            stmt.setString(3, notes);
            stmt.setLong(4, startDate);
            stmt.setLong(5, endDate);
            stmt.setString(6, id);
            int rows = stmt.executeUpdate();
            if (rows == 1)
                event = getEventbyId(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return event;
    }



    @Override
    public boolean deleteEvent(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.DELETE_EVENT);
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
    public boolean deleteParticipants(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.DELETE_PARTICIPANTS_EVENT);
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
}
