package org.jsp.jsp_19_sgnr.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("JDBC 드라이버 해제 시작");

        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                    System.out.println("JDBC 드라이버 해제 성공");
                } catch (SQLException e) {
                    System.out.println("JDBC 드라이버 해제 실패");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("드라이버 해제 중 예외 발생");
            e.printStackTrace();
        }
    }
}
