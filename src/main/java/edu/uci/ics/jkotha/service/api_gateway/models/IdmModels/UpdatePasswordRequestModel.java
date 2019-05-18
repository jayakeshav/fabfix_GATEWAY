package edu.uci.ics.jkotha.service.api_gateway.models.IdmModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class UpdatePasswordRequestModel extends RequestModel {
    private String email;
    private char[] oldpword;
    private char[] newpword;

    @JsonCreator
    public UpdatePasswordRequestModel(
            @JsonProperty(value = "email",required = true) String email,
            @JsonProperty(value = "oldpword",required = true) char[] oldpword,
            @JsonProperty(value = "newpword",required = true) char[] newpword) {
        this.email = email;
        this.oldpword = oldpword;
        this.newpword = newpword;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public char[] getOldpword() {
        return oldpword;
    }

    @JsonProperty
    public char[] getNewpword() {
        return newpword;
    }
}
