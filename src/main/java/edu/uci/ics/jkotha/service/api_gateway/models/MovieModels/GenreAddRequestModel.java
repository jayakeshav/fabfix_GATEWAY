package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class GenreAddRequestModel extends RequestModel {
    @JsonProperty(value = "name",required = true)
    private String name;

    @JsonCreator
    public GenreAddRequestModel(@JsonProperty(value = "name",required = true) String name) {
        this.name = name;
    }

    @JsonProperty
    public String getName() { return name; }
}
