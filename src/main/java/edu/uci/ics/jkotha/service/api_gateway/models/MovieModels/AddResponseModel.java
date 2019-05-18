package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddResponseModel extends RequestModel {

    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    private String movieid;
    private int[] genreid;


    @JsonCreator
    public AddResponseModel(int resultCode, String message, String movieid, int[] genreid) {
        this.resultCode = resultCode;
        this.message = message;
        this.movieid = movieid;
        this.genreid = genreid;
    }

    @JsonProperty
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public String getMovieid() {
        return movieid;
    }

    @JsonProperty
    public int[] getGenreid() {
        return genreid;
    }
}
