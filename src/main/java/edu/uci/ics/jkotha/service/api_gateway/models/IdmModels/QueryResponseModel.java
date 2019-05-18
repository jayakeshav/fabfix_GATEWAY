package edu.uci.ics.jkotha.service.api_gateway.models.IdmModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryResponseModel extends ResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    private UserModel[] user;

    @JsonCreator
    public QueryResponseModel(
            int resultCode,
            String message,
            UserModel[] user) {
        this.resultCode = resultCode;
        this.message = message;
        this.user = user;
    }

    @JsonCreator
    public QueryResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
        this.user = null;
    }

    @JsonCreator

    @JsonProperty
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public UserModel[] getUser() {
        return user;
    }
}
