package edu.upc.eetac.dsa.walka.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by SergioGM on 05.12.15.
 */
public class Database {

        private static Database instance = null;
        private DataSource ds;

        private Database() {
            HikariConfig config = new HikariConfig(Database.class.getClassLoader().getResource("hikari.properties").getFile());
            ds = new HikariDataSource(config);
        }

        private final static Database getInstance() {
            if (instance == null)
                instance = new Database();
            return instance;
        }

        public final static Connection getConnection() throws SQLException {
            return getInstance().ds.getConnection();
        }

}
