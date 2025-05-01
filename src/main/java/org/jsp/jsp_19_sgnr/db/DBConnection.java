package org.jsp.jsp_19_sgnr.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:oracle:thin:@dinkdb_medium?TNS_ADMIN=/Users/mbp/Downloads/Wallet_DinkDB");
        config.setUsername("DA2516");
        config.setPassword("Data2516");
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setMaximumPoolSize(3);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(600000);
        config.setConnectionTimeout(10000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
