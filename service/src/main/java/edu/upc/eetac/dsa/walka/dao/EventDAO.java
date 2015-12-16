package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.entity.Event;
import edu.upc.eetac.dsa.walka.entity.EventCollection;

import java.sql.SQLException;

/**
 * Created by SergioGM on 06.12.15.
 */
public interface EventDAO {
    public Event createEvent(String userid, String title, String location, String notes, long startDate, long endDate) throws SQLException;
    public Event getEventbyId(String id) throws SQLException;
    /**Obtener por mes, envio primer dia de mes y me devuelve el intervalo de un mes ¿?*/
    public EventCollection getEventsMonth();
    /**Obtener por dia*/
    public EventCollection getEventsDay();
    public boolean JoinEvent(String userid, String eventid) throws SQLException;
    public boolean checkUserInEvent(String eventid, String userid) throws SQLException;
    public boolean LeaveEvent(String userid, String eventid) throws SQLException;
    public EventCollection getEventsFromTo(long fromData, long toData) throws SQLException;
    public Event updateEvent(String id, String content, String location, String notes, long startDate, long endDate) throws SQLException;
    public boolean deleteEvent(String id) throws SQLException;
    public boolean deleteParticipants(String id) throws SQLException;

}
