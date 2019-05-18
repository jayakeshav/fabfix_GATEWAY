package edu.uci.ics.jkotha.service.api_gateway.models.IdmModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionResponseModel extends ResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    private String sessionID;

    @JsonCreator
    public SessionResponseModel(
            int resultCode,
            String message,
            String sessionID) {
        this.resultCode = resultCode;
        this.message = message;
        this.sessionID = sessionID;
    }

    @JsonCreator
    public SessionResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
        this.sessionID = null;
    }

    @JsonProperty
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public String getSessionID() {
        return sessionID;
    }
}
