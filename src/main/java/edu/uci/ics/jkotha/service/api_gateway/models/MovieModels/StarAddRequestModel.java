package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class StarAddRequestModel extends RequestModel {
    @JsonProperty(value = "name",required = true)
    private String name;
    @JsonProperty(value = "birthYear", required = true)
    private String birthYear;

    @JsonCreator
    public StarAddRequestModel(
            @JsonProperty(value = "name",required = true) String name,
            @JsonProperty(value = "birthYear", required = true) String birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    @JsonProperty
    public String getName() { return name; }

    @JsonProperty
    public String getBirthYear() {
        return birthYear;
    }
}
