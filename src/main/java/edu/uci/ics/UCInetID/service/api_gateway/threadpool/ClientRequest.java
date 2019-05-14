package edu.uci.ics.UCInetID.service.api_gateway.threadpool;

import edu.uci.ics.UCInetID.service.api_gateway.models.RequestModel;

public class ClientRequest {
    private String email;
    private String sessionID;
    private String transactionID;
    private RequestModel request;
    private String URI;
    private String endpoint;

    public ClientRequest() {

    }
}
