package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.db.Database;
import edu.upc.eetac.dsa.walka.entity.Event;
import edu.upc.eetac.dsa.walka.entity.EventCollection;
import edu.upc.eetac.dsa.walka.entity.UserCollection;

import java.sql.*;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by SergioGM on 07.12.15.
 */
public class EventDAOImpl implements EventDAO {
    @Override
    public Event createEvent(String userid, String title, String location, String notes, String startDate, String endDate) throws SQLException {
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
            stmt.setString(6, startDate);
            stmt.setString(7, endDate);
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
                event.setNotes(rs.getString("notes"));
                /**Obtengo participantes de otra clase*/
                UserCollection participants = userDAO.getParticipantsByEventId(id);
                event.setParticipants(participants);
                event.setStart(rs.getString("startdate"));
                event.setEnd(rs.getString("enddate"));
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

    //Obtiene todos los eventos de un usuario en un cierto mes
    @Override
    public EventCollection getEventsMonth(String iduser, String monthDate) throws SQLException {
        EventCollection eventCollection = new EventCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        UserCollectionDAO userDAO = new UserCollectionDAOImpl();
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.GET_EVENTS_BY_MONTH);
            stmt.setString(1, iduser);
            stmt.setString(2, monthDate);
            stmt.setString(3, monthDate);


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Event event = new Event();

                event.setId(rs.getString("id"));
                event.setTitle(rs.getString("title"));
                event.setCreator(rs.getString("creator"));
                event.setLocation(rs.getString("location"));
                /**Obtengo participantes de otra clase*/
                UserCollection participants = userDAO.getParticipantsByEventId(event.getId());
                event.setParticipants(participants);
                event.setStart(rs.getString("startdate"));
                event.setEnd(rs.getString("enddate"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());

                eventCollection.getEvents().add(event);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return eventCollection;
    }
    //Obtiene eventos de un usuario en un cierto dia
    @Override
    public EventCollection getEventsDay(String iduser, String dayDate) throws SQLException {
        EventCollection eventCollection = new EventCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        UserCollectionDAO userDAO = new UserCollectionDAOImpl();
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.GET_EVENTS_BY_DAY);
            stmt.setString(1, iduser);
            stmt.setString(2, dayDate);
            stmt.setString(3, dayDate);


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Event event = new Event();

                event.setId(rs.getString("id"));
                event.setTitle(rs.getString("title"));
                event.setCreator(rs.getString("creator"));
                event.setLocation(rs.getString("location"));
                /**Obtengo participantes de otra clase*/
                UserCollection participants = userDAO.getParticipantsByEventId(event.getId());
                event.setParticipants(participants);
                event.setStart(rs.getString("startdate"));
                event.setEnd(rs.getString("enddate"));
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                event.setLastModified(rs.getTimestamp("last_modified").getTime());

                eventCollection.getEvents().add(event);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return eventCollection;
    }

    //Obtiene todos los eventos del usuario
    @Override
    public EventCollection getAllEvents(String iduser) throws SQLException {
        EventCollection eventCollection = new EventCollection();
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        UserCollectionDAO userDAO = new UserCollectionDAOImpl();
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.GET_EVENTS_BY_USERID);
            stmt.setString(1,iduser);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                event = new Event();

                event.setId(rs.getString("id"));
                System.out.println(event.getId());
                event.setTitle(rs.getString("title"));
                System.out.println(event.getTitle());
                event.setCreator(rs.getString("creator"));
                System.out.println(event.getCreator());
                event.setLocation(rs.getString("location"));
                System.out.println(event.getLocation());
                event.setNotes(rs.getString("notes"));
                System.out.println(event.getNotes());
                /**Obtengo participantes de otra clase*/
                UserCollection participants = userDAO.getParticipantsByEventId(event.getId());
                event.setParticipants(participants);
                event.setStart(rs.getString("startdate"));
                System.out.println(event.getStart());
                event.setEnd(rs.getString("enddate"));
                System.out.println(event.getEnd());
                event.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                //Creación url
                PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("walka");
                String baseURI = prb.getString("walka.eventsurl");
                String eventurl = baseURI + "/" + event.getId();
                event.setUrl(eventurl);
                event.setLastModified(rs.getTimestamp("last_modified").getTime());


                eventCollection.getEvents().add(event);

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return eventCollection;
    }

    //Solo el creador de evento puede añadir participantes
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

    //Cualquier participante puede dejar el evento, excepto el creador, que no puede dejar el evento.
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
    public EventCollection getEventsFromTo(String fromData, String toData) throws SQLException {
        return null;
    }

    @Override
    public Event updateEvent(String id, String title, String location, String notes, String startDate, String endDate) throws SQLException {
        Event event = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.UPDATE_EVENT);

            stmt.setString(1, title);
            stmt.setString(2, location);
            stmt.setString(3, notes);
            stmt.setString(4, startDate);
            stmt.setString(5, endDate);
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
    public boolean checkUserInEvent(String eventid, String userid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {

            connection = Database.getConnection();

            stmt = connection.prepareStatement(EventDAOQuery.CHECK_USER_IN_EVENT);
            stmt.setString(1, userid);
            stmt.setString(2, eventid);

            ResultSet rs= stmt.executeQuery();

            return  (rs.next());

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

    }

    //Solo puede eliminar un evento su creador
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
