package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.entity.Group;
import edu.upc.eetac.dsa.walka.entity.InvitationCollection;

import java.sql.SQLException;

/**
 * Created by SergioGM on 09.01.16.
 */
public interface GroupDAO {
    public Group createGroup(String creator, String name, String description) throws SQLException;
    public Group getGroupbyId(String id) throws SQLException;
    public Group updateGroup(String id, String name, String description) throws SQLException;
    public boolean deleteGroup(String id) throws SQLException;
    public boolean checkUserInGroup(String idgroup, String iduser) throws SQLException;
    public boolean checkUserInInvitation(String idgroup, String iduser) throws SQLException;
    public InvitationCollection checkInvitations(String userid) throws SQLException;
    public boolean addUserToGroup(String idgroup, String iduser) throws SQLException;
    public boolean deleteUserFromGroup(String idgroup, String iduser) throws SQLException;
    public boolean inviteUserToGroup(String idgroup, String iduser) throws SQLException;
    public boolean deleteUserFromInvitations(String idgroup, String userInvited) throws SQLException;
    public boolean addGroupMembersToEvent(String idgroup, String idevent) throws SQLException;
    public boolean groupIsFull(String idgroup) throws SQLException;



}
