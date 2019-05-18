package edu.uci.ics.jkotha.service.api_gateway.models.BillingModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class JustId extends RequestModel {
    @JsonProperty(value = "id", required = true)
    private String id;

    @JsonCreator
    public JustId(
            @JsonProperty(value = "id", required = true) String id) {
        this.id = id;
    }

    @JsonProperty
    public String getId() {
        return id;
    }
}
