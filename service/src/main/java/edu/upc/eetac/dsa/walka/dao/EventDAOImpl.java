package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.entity.Event;
import edu.upc.eetac.dsa.walka.entity.EventCollection;

import java.sql.SQLException;

/**
 * Created by SergioGM on 07.12.15.
 */
public class EventDAOImpl implements EventDAO {
    @Override
    public Event createEvent(String userid, String title, String location, String notes, long startDate, long endDate) throws SQLException {
        return null;
    }

    @Override
    public Event getEventbyId(String id) throws SQLException {
        return null;
    }

    @Override
    public EventCollection getEventsMonth() {
        return null;
    }

    @Override
    public EventCollection getEventsWeek() {
        return null;
    }

    @Override
    public EventCollection getEventsDay() {
        return null;
    }

    @Override
    public boolean JoinEvent(String userid, String eventid) throws SQLException{
        return false;
    }

    @Override
    public boolean LeaveEvent(String userid, String eventid) throws SQLException{
        return false;
    }

    @Override
    public EventCollection getEventsFromTo(long fromData, long toData) throws SQLException {
        return null;
    }

    @Override
    public Event updateEvent(String id, String title, String location, String notes, long startDate, long endDate) throws SQLException {
        return null;
    }

    @Override
    public boolean deleteEvent(String id) throws SQLException {
        return false;
    }
}
