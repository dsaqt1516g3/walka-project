package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.entity.Event;
import edu.upc.eetac.dsa.walka.entity.EventCollection;

import java.sql.SQLException;

/**
 * Created by SergioGM on 06.12.15.
 */
public interface EventDAO {
    public Event createEvent(String userid, String title, String location, String notes, String tag, String startDate, String endDate) throws SQLException;
    public Event getEventbyId(String id) throws SQLException;
    /**Obtener por mes, envio primer dia de mes y me devuelve el intervalo de un mes*/
    public EventCollection getEventsMonth(String iduser, String monthDate) throws SQLException;
    /**Obtener por dia*/
    public EventCollection getEventsBetween(String iduser, String start, String end) throws SQLException;
    public EventCollection getEventsDay(String iduser, String dayDate) throws SQLException;
    public EventCollection getAllEvents(String iduser) throws SQLException;
    public boolean JoinEvent(String userid, String eventid) throws SQLException;
    public boolean checkUserInEvent(String eventid, String userid) throws SQLException;
    public boolean LeaveEvent(String userid, String eventid) throws SQLException;
    public Event updateEvent(String id, String content, String location, String notes,String tag, String startDate, String endDate) throws SQLException;
    public boolean deleteEvent(String id) throws SQLException;
    public boolean deleteParticipants(String id) throws SQLException;
    public String getColour(String idevent, String iduser) throws SQLException;
    public boolean modifyColour(String colour, String idevent, String iduser) throws SQLException;
    public boolean eventIsFull(String idevent) throws SQLException;
    public EventCollection searchEvents(String keyword, String iduser) throws SQLException;

}
