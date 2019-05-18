package edu.uci.ics.jkotha.service.api_gateway.models.IdmModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class UpdateRequestModel extends RequestModel {
    private int id;
    private String email;
    private String plevel;

    @JsonCreator
    public UpdateRequestModel(
            @JsonProperty(value = "id",required = true) int id,
            @JsonProperty(value = "email") String email,
            @JsonProperty(value = "plevel") String plevel) {
        this.id = id;
        this.email = email;
        this.plevel = plevel;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public String getPlevel() {
        return plevel;
    }
}
