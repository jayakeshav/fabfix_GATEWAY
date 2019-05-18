package edu.uci.ics.jkotha.service.api_gateway.models.BillingModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPlaceResponseModel extends ResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "redirectURL")
    private String redirectURL;
    @JsonProperty(value = "token")
    private String token;

    @JsonCreator
    public OrderPlaceResponseModel(int resultCode, String message, String redirectURL, String token) {
        this.resultCode = resultCode;
        this.message = message;
        this.redirectURL = redirectURL;
        this.token = token;
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
    public String getRedirectURL() {
        return redirectURL;
    }

    @JsonProperty
    public String getToken() {
        return token;
    }
}
