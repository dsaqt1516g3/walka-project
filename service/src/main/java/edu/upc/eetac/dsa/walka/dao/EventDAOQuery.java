package edu.upc.eetac.dsa.walka.dao;

/**
 * Created by SergioGM on 07.12.15.
 */
public class EventDAOQuery {

    /**OK*/public final static String UUID = "select REPLACE(UUID(),'-','')";
    /**OK*/public final static String CREATE_EVENT = "insert into events (id, title, creator, location, notes, startdate, enddate) values (UNHEX(?), ?, unhex(?), ?, ?, ?, ?)";
    /**OK*/public final static String GET_EVENT_BY_ID = "select hex(e.id) as id, e.title, hex(e.creator) as creator, e.location, e.notes, e.startdate, e.enddate, e.creation_timestamp, e.last_modified, u.fullname from events e, users u where e.id=unhex(?) and u.id=e.creator";
   /**Corregir*/public final static String GET_EVENTS_BETWEEN = "SELECT e.* FROM events AS e INNER JOIN user_events AS ue ON e.id = ue.idevent WHERE ue.iduser = unhex(?) AND (e.startdate BETWEEN ? AND ?)";
    /**OK*/public final static String UPDATE_EVENT = "update events set title=?, location=?, notes=?, startdate=?, enddate=? where id=unhex(?)";
    /**OK*/public final static String DELETE_EVENT = "delete from events where id=unhex(?)";
    public final static String DELETE_PARTICIPANTS_EVENT = "delete from user_events where idevent=unhex(?)";
    /**OK*/public final static String JOIN_EVENT = "insert into user_events (idevent, iduser) values (UNHEX(?),unhex(?))";
    /**OK*/public final static String LEAVE_EVENT = "delete from events where idevent=unhex(?) AND iduser=unhex(?)";
}
