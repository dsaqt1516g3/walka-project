package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.entity.UserCollection;

import java.sql.SQLException;

/**
 * Created by SergioGM on 07.12.15.
 */
public interface UserCollectionDAO {
    public UserCollection getParticipantsByEventId(String eventid) throws SQLException;
    public UserCollection getUsersByGroupId(String eventid) throws SQLException;
}
