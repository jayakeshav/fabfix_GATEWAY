package edu.uci.ics.UCInetID.service.api_gateway.connectionpool;

import java.sql.Connection;
import java.util.LinkedList;

public class ConnectionPool {
    LinkedList<Connection> connections;
    String driver;
    String url;
    String username;
    String password;

    public ConnectionPool(int numCons, String driver, String url, String username, String password) {

    }

    public Connection requestCon() {

    }

    public void releaseCon(Connection con) {

    }

    private Connection createConnection() {

    }
}
