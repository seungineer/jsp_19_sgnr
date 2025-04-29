package org.jsp.jsp_19_sgnr.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@dinkdb_medium?TNS_ADMIN=/Users/mbp/Downloads/Wallet_DinkDB";
    private static final String USER = "DA2516";
    private static final String PASSWORD = "Data2516";

    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver"); // 드라이버 로딩
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
