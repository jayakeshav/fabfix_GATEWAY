package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreModel {
    @JsonProperty(required = true)
    private int id;
    @JsonProperty(required = true)
    private String name;

    @JsonCreator
    public GenreModel(
            @JsonProperty(value = "id",required = true) int id,
            @JsonProperty(value = "name",required = true) String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}
