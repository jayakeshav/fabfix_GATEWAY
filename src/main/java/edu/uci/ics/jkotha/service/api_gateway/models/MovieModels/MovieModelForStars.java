package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieModelForStars {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String title;

    @JsonCreator
    public MovieModelForStars(String id, String TITLE) {
        this.id = id;
        this.title = TITLE;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }
}
