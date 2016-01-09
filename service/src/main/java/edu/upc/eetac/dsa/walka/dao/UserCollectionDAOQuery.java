package edu.upc.eetac.dsa.walka.dao;

/**
 * Created by SergioGM on 07.12.15.
 */
public interface UserCollectionDAOQuery {
    public final static String GET_PARTICIPANTS_BY_EVENT_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname from users u INNER JOIN user_events as ue ON u.id = ue.iduser WHERE ue.idevent = unhex(?)";
    public final static String GET_USERS_BY_GROUP_ID ="select hex(u.id) as id, u.loginid, u.email, u.fullname from users u INNER JOIN user_groups as ug ON u.id = ug.iduser WHERE ug.idgroup = unhex(?)";
}
