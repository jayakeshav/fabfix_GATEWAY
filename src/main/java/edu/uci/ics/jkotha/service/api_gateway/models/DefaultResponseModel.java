package edu.uci.ics.jkotha.service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;
import edu.uci.ics.jkotha.service.api_gateway.utilities.ResultCodes;

public class DefaultResponseModel extends ResponseModel {
    private int resultCode;
    private String message;

    @JsonCreator
    public DefaultResponseModel(
            @JsonProperty(value = "resultCode",required = true) int resultCode,
            @JsonProperty(value = "message",required = true) String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public DefaultResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = ResultCodes.setMessage(resultCode);
    }

    @JsonProperty
    public int getResultCode() {
        return resultCode;
    }
    @JsonProperty
    public String getMessage() {
        return message;
    }
}
