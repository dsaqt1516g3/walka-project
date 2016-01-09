package edu.upc.eetac.dsa.walka.dao;

import edu.upc.eetac.dsa.walka.entity.Group;

import java.sql.SQLException;

/**
 * Created by SergioGM on 09.01.16.
 */
public interface GroupDAO {
    public Group createGroup(String creator, String name, String description) throws SQLException;
    public Group getGroupbyId(String id) throws SQLException;
    public Group updateGroup(String id, String creator, String name, String description) throws SQLException;
    public boolean deleteGroup(String id) throws SQLException;


}
