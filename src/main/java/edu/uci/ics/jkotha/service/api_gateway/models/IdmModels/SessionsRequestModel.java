package edu.uci.ics.jkotha.service.api_gateway.models.IdmModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class SessionsRequestModel extends RequestModel {
    private String email;
    private String sessionID;

    @JsonCreator
    public SessionsRequestModel(
            @JsonProperty(value = "email",required = true) String email,
            @JsonProperty(value = "sessionID",required = true) String sessionID) {
        this.email = email;
        this.sessionID = sessionID;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public String getSessionID() {
        return sessionID;
    }
}
