package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class RemoveRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String id;

    @JsonCreator
    public RemoveRequestModel(
            @JsonProperty(value = "id",required = true) String id) {
        this.id = id;
    }

    @JsonProperty
    public String getId() {
        return id;
    }
}
