package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarsSearchResponseModel extends ResponseModel {
    @JsonProperty(required = true)
    private int resultcode;
    @JsonProperty(required = true)
    private String message;
    private StarModel[] stars;

    @JsonCreator
    public StarsSearchResponseModel(int resultcode, String message, StarModel[] stars) {
        this.resultcode = resultcode;
        this.message = message;
        this.stars = stars;
    }

    @JsonProperty
    public int getResultcode() {
        return resultcode;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public StarModel[] getStars() {
        return stars;
    }
}
