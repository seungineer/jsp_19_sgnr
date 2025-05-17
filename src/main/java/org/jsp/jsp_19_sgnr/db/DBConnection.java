package org.jsp.jsp_19_sgnr.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    /**
     * URL 주소는 local Wallet 주소에 맞춰서 설정해야 합니다.
     * config: jdbc:oracle:thin:@dinkdb_medium
     **/
    private static final String URL = "jdbc:oracle:thin:@dinkdb_medium?TNS_ADMIN=/Users/mbp/Downloads/Wallet_DinkDB";
    private static final String USERNAME = "DA2516";   // 노출 가능한 id
    private static final String PASSWORD = "Data2516"; // 노출 가능한 pw

    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("JDBC 드라이버 로드 실패");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
