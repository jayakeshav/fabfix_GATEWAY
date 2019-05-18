package edu.uci.ics.jkotha.service.api_gateway.threadpool;

import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

import java.util.Map;

public class ClientRequest {
    private String email;
    private String sessionID;
    private String transactionID;
    private RequestModel request;
    private String URI;
    private String endpoint;
    private String method;
    private Map<String, Object> pathParam;
    private Map<String,Object> queryParamValues;

    public ClientRequest() {

    }

    public ClientRequest(String email, String sessionID, String transactionID, RequestModel request, String URI, String endpoint, String method) {
        this.email = email;
        this.sessionID = sessionID;
        this.transactionID = transactionID;
        this.request = request;
        this.URI = URI;
        this.endpoint = endpoint;
        this.method = method;
    }

    public String getEmail() {
        return email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public RequestModel getRequest() {
        return request;
    }

    public String getURI() {
        return URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getPathParam() { return pathParam; }

    public void setPathParam(Map<String, Object> pathParam) { this.pathParam = pathParam; }

    public Map<String, Object> getQueryParamValues() { return queryParamValues; }

    public void setQueryParamValues(Map<String, Object> queryParamValues) { this.queryParamValues = queryParamValues; }
}
