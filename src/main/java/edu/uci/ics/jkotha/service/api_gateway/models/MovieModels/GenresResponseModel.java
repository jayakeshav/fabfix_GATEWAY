package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenresResponseModel extends ResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "genres")
    private GenreModel[] genres;

    @JsonCreator
    public GenresResponseModel(int resultCode, String message, GenreModel[] genres) {
        this.resultCode = resultCode;
        this.message = message;
        this.genres = genres;
    }



    @JsonProperty
    public int getResultCode() { return resultCode; }

    @JsonProperty
    public String getMessage() { return message; }

    @JsonProperty
    public GenreModel[] getGenres() { return genres; }
}
