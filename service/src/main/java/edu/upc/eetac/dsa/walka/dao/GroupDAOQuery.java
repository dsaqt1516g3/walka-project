package edu.upc.eetac.dsa.walka.dao;

/**
 * Created by SergioGM on 09.01.16.
 */
public interface GroupDAOQuery {

    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_GROUP = "insert into groups (id, idcreator, name, description) values (UNHEX(?), UNHEX(?), ?, ?)";
    public final static String UPDATE_GROUP = "update groups set name=?, description=? where id=unhex(?)";
    public final static String DELETE_GROUP = "delete from groups where id=unhex(?)";
    public final static String GET_GROUP_BY_ID = "select hex(g.id) as id, hex(g.idcreator) as idcreator, g.name, g.description, g.creation_timestamp, g.last_modified from groups g where id=unhex(?)";
    //Obtener participantes de un grupo se encuentra en UserCollection
    public final static String GET_GROUPS_USERID = "select hex(g.id) as id, hex(g.idcreator) as creator, g.name, g.description ,g.creation_timestamp from groups g INNER JOIN user_groups AS ug ON g.id = ug.idgroup WHERE ug.iduser=unhex(?) AND creation_timestamp < ? order by creation_timestamp desc limit 15";
    public final static String GET_GROUPS_USERID_AFTER = "select hex(g.id) as id, hex(g.idcreator) as creator, g.name, g.description ,g.creation_timestamp from groups g INNER JOIN user_groups AS ug ON g.id = ug.idgroup WHERE ug.iduser=unhex(?) AND creation_timestamp > ? order by creation_timestamp desc limit 15";
    //Invitaciones
    public final static String INVITE_USERID_TO_GROUP = "insert into invitations (groupid, userInvited) values (unhex(?),unhex(?))";
    public final static String DELETE_USER_FROM_PENDING_INVITATIONS = "delete from invitations where groupid=unhex(?) AND userInvited=unhex(?)";
    public final static String CHECK_INVITATIONS = "select hex(groupid) as groupid,hex(userInvited) as userInvited from invitations where userInvited=unhex(?)";
    //Añadir o eliminar de grupos
    public final static String ADD_USER_TO_GROUP = "insert into user_groups (idgroup,iduser) values (unhex(?),unhex(?))";
    public final static String DELETE_USER_FROM_GROUP = "delete from user_groups where idgroup=unhex(?) AND iduser=unhex(?)";
    public final static String CHECK_USER_IN_GROUP = "select * from user_groups where iduser=unhex(?) AND idgroup=unhex(?)";
    public final static String CHECK_IF_USER_IS_INVITED_TO_GROUP = "select * from invitations where groupid=unhex(?) AND userInvited=unhex(?)";

    public final static String GET_NUMBER_PARTICIPANTS_GROUP = "select count(*) as participants from user_groups where idgroup=unhex(?)";
    public final static int MAX_NUMBER_PEOPLE_GROUP = 20;


}
