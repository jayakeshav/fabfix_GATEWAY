package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarModelForMovies {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String name;

    @JsonCreator
    public StarModelForMovies(String id, String name) {
        this.id = id;
        this.name = name;

    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

}
