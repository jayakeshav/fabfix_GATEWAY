package edu.uci.ics.jkotha.service.api_gateway.models.BillingModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;

public class JustEmailReqModel extends RequestModel {
    @JsonProperty
    private String email;

    @JsonCreator
    public JustEmailReqModel(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }
}
