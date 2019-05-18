package edu.uci.ics.jkotha.service.api_gateway.models.IdmModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class RegisterRequestModel extends RequestModel {
    private String email;
    private char[] password;

    @JsonCreator
    public RegisterRequestModel(
            @JsonProperty(value = "email",required = true) String email,
            @JsonProperty(value = "password",required = true) char[] password) {
        this.email = email;
        this.password = password;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public char[] getPassword() {
        return password;
    }
}
