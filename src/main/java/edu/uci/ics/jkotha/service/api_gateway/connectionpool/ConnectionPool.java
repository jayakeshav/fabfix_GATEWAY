package edu.uci.ics.jkotha.service.api_gateway.connectionpool;

import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool extends Thread{
    private LinkedList<Connection> connections;
    private String driver;
    private String url;
    private String username;
    private String password;

    public ConnectionPool(int numCons, String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username=username;
        this.password=password;
        connections = new LinkedList<>();
        for (int i = 0;i<numCons;i++){
            connections.push(createConnection());
        }
    }

    public Connection requestCon() {
        if (connections.size() != 0)
            return connections.pop();
        else
            return createConnection();
    }

    public void releaseCon(Connection con) {
        connections.push(con);
    }

    private Connection createConnection() {
        Connection con = null;
        try{
        Class.forName(driver);
        ServiceLogger.LOGGER.config("Database URL: " +url);
        con = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException | SQLException | NullPointerException e) {
            ServiceLogger.LOGGER.severe("Unable to connect to database.\n" + ExceptionUtils.exceptionStackTraceAsString(e));
        }
        return con;
    }

}
