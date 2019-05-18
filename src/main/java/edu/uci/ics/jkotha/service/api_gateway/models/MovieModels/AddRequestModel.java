package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddRequestModel extends RequestModel {
    @JsonProperty
    private String title;
    @JsonProperty
    private String director;
    @JsonProperty
    private int year;
    @JsonProperty
    private String backdrop_path;
    @JsonProperty
    private int budget;
    @JsonProperty
    private String overview;
    @JsonProperty
    private String poster_path;
    @JsonProperty
    private int revenue;
    @JsonProperty
    private GenreModel[] genres;

    @JsonCreator
    public AddRequestModel() {
    }

    @JsonCreator
    public AddRequestModel(
            @JsonProperty(value = "title",required = true) String title,
            @JsonProperty(value = "director",required = true) String director,
            @JsonProperty(value = "year",required = true) int year,
            @JsonProperty(value = "backdrop_path") String backdrop_path,
            @JsonProperty(value = "budget") Integer budget,
            @JsonProperty(value = "overview") String overview,
            @JsonProperty(value = "poster_path") String poster_path,
            @JsonProperty(value = "revenue") Integer revenue,
            @JsonProperty(value = "genres",required = true) GenreModel[] genres) {

        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        if (budget==null)
            this.budget=0;
        else
            this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        if (revenue==null)
            this.revenue=0;
        else
            this.revenue = revenue;
        this.genres = genres;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonCreator
    public String getDirector() {
        return director;
    }

    @JsonProperty
    public int getYear() {
        return year;
    }

    @JsonCreator
    public String getBackdrop_path() {
        return backdrop_path;
    }

    @JsonProperty
    public int getBudget() {
        return budget;
    }

    @JsonProperty
    public String getOverview() {
        return overview;
    }

    @JsonProperty
    public String getPoster_path() {
        return poster_path;
    }

    @JsonProperty
    public int getRevenue() {
        return revenue;
    }

    @JsonProperty
    public GenreModel[] getGenres() {
        return genres;
    }
}
