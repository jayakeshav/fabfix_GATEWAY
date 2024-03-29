package edu.uci.ics.jkotha.service.api_gateway.models.IdmModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class PrivilegeRequestModel extends RequestModel {
    private String email;
    private int plevel;

    @JsonCreator
    public PrivilegeRequestModel(
            @JsonProperty(value = "email",required = true) String email,
            @JsonProperty(value = "plevel",required = true) int plevel) {
        this.email = email;
        this.plevel = plevel;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public int getPlevel() {
        return plevel;
    }

    @Override
    public String toString() {
        return "email: "+this.email+" plevel: "+this.plevel;
    }
}
