package edu.upc.eetac.dsa.walka.dao;

/**
 * Created by SergioGM on 07.12.15.
 */
public interface EventDAOQuery {

    /**OK*/public final static String UUID = "select REPLACE(UUID(),'-','')";
    /**OK*/public final static String CREATE_EVENT = "insert into events (id, title, creator, location, notes, startdate, enddate, tag) values (UNHEX(?), ?, unhex(?), ?, ?, ?, ?, ?)";
    public final static String GET_EVENTS_BY_USERID = "SELECT hex(e.id) as id, e.title, hex(e.creator) as creator, e.location, e.notes, e.tag, e.startdate, e.enddate, e.creation_timestamp, e.last_modified FROM events AS e INNER JOIN user_events AS ue ON e.id = ue.idevent WHERE ue.iduser = unhex(?)";
    /**OK*/public final static String GET_EVENT_BY_ID = "select hex(e.id) as id, e.title, hex(e.creator) as creator, e.location, e.notes, e.tag, e.startdate, e.enddate, e.creation_timestamp, e.last_modified, u.fullname from events e, users u where e.id=unhex(?) and u.id=e.creator";
   /**Corregir*/public final static String GET_EVENTS_BETWEEN = "SELECT hex(e.id) as id, e.title, hex(e.creator) as creator, e.location, e.notes, e.tag, e.startdate, e.enddate, e.creation_timestamp, e.last_modified FROM events AS e INNER JOIN user_events AS ue ON e.id = ue.idevent WHERE ue.iduser = unhex(?) AND (e.startdate BETWEEN ? AND ?)";
    public final static String GET_EVENTS_BY_MONTH = "SELECT hex(e.id) as id, e.title, hex(e.creator) as creator, e.location, e.notes, e.tag, e.startdate, e.enddate, e.creation_timestamp, e.last_modified FROM events AS e INNER JOIN user_events AS ue ON e.id = ue.idevent WHERE ue.iduser = unhex(?) AND (e.startdate BETWEEN ? AND date_add(?, INTERVAL 1 MONTH)) order by startdate asc";
    public final static String GET_EVENTS_BY_DAY = "SELECT hex(e.id) as id, e.title, hex(e.creator) as creator, e.location, e.notes, e.tag, e.startdate, e.enddate, e.creation_timestamp, e.last_modified FROM events AS e INNER JOIN user_events AS ue ON e.id = ue.idevent WHERE ue.iduser = unhex(?) AND (e.startdate BETWEEN ? AND date_add(?, INTERVAL 1 DAY)) order by startdate asc";
    public final static String GET_EVENTS_BY_WEEK = "SELECT hex(e.id) as id, e.title, hex(e.creator) as creator, e.location, e.notes, e.tag, e.startdate, e.enddate, e.creation_timestamp, e.last_modified FROM events AS e INNER JOIN user_events AS ue ON e.id = ue.idevent WHERE ue.iduser = unhex(?) AND (e.startdate BETWEEN ? AND date_add(?, INTERVAL 1 WEEK)) order by startdate asc";
    //Obtener participantes de un evento se encuentra en UserCollection
    /**OK*/public final static String UPDATE_EVENT = "update events set title=?, location=?, notes=?, tag=?, startdate=?, enddate=? where id=unhex(?)";
    /**OK*/public final static String DELETE_EVENT = "delete from events where id=unhex(?)";
    public final static String CHECK_USER_IN_EVENT = "select * from user_events where iduser=unhex(?) AND idevent=unhex(?)";
    public final static String DELETE_PARTICIPANTS_EVENT = "delete from user_events where idevent=unhex(?)";
    /**OK*/public final static String JOIN_EVENT = "insert into user_events (idevent, iduser) values (UNHEX(?),unhex(?))";
    /**OK*/public final static String LEAVE_EVENT = "delete from user_events where idevent=unhex(?) AND iduser=unhex(?)";
    public final static String MODIFY_COLOUR = "UPDATE user_events SET colour=? WHERE idevent=unhex(?) AND iduser=unhex(?)";
    public final static String GET_COLOUR = "select colour from user_events where idevent=unhex(?) AND iduser=unhex(?)";
    public final static String GET_NUMBER_PARTICIPANTS_EVENT = "select count(*) as participants from user_events where idevent=unhex(?)";
    public final static int MAX_NUMBER_PEOPLE_EVENT = 50;
    public final static String SEARCH_EVENT ="SELECT hex(e.id) as id, e.title, hex(e.creator) as creator, e.location, e.notes, e.tag, e.startdate, e.enddate, e.creation_timestamp, e.last_modified FROM events AS e INNER JOIN user_events AS ue ON e.id = ue.idevent WHERE ue.iduser = unhex(?) AND title REGEXP ? OR location REGEXP ? OR notes REGEXP ? OR tag REGEXP ?";
}
