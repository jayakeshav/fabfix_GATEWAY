package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

public class RatingsReqModel extends RequestModel {
    @JsonProperty(value = "id",required = true)
    private String id;
    @JsonProperty(value = "rating",required = true)
    private float rating;

    @JsonCreator
    public RatingsReqModel(
            @JsonProperty(value = "id",required = true) String id,
            @JsonProperty(value = "rating",required = true) float rating) {
        this.id = id;
        this.rating = rating;
    }

    @JsonProperty
    public String getId() { return id; }

    @JsonProperty
    public float getRating() { return rating; }
}
