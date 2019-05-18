package edu.uci.ics.jkotha.service.api_gateway.models.MovieModels;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieModel {
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private String title;
    @JsonProperty(required = true)
    private String director;
    private Integer year;
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    private Float rating;
    private Integer numVotes;
    private Boolean hidden;
    @JsonProperty(required = true)
    private GenreModel[] genres;
    @JsonProperty(required = true)
    private StarModelForMovies[] stars;


    @JsonCreator
    public MovieModel(String id, String title, String director, int year,
                      String backdrop_path, Integer budget, String overview,
                      String poster_path, Integer revenue, float rating, int numVotes, GenreModel[] genres, StarModelForMovies[] stars,
                      Integer hidden) {
        this.movieId = id;
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        if (budget ==0)
            this.budget=null;
        else
            this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        if (revenue==0)
            this.revenue =null;
        else
            this.revenue = revenue;
        this.rating = rating;
        this.numVotes = numVotes;
        this.genres = genres;
        this.stars = stars;
        if (hidden==null)
            this.hidden = null;
        else if (hidden==0)
            this.hidden = false;
        else if (hidden == 1)
            this.hidden = true;

    }

    @JsonProperty
    public String getMovieId() {
        return movieId;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public String getDirector() {
        return director;
    }

    @JsonProperty
    public Integer getYear() {
        return year;
    }

    @JsonProperty
    public String getBackdrop_path() {
        return backdrop_path;
    }

    @JsonProperty
    public Integer getBudget() {
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
    public Integer getRevenue() {
        return revenue;
    }

    @JsonProperty
    public Float getRating() {
        return rating;
    }

    @JsonProperty
    public Integer getNumVotes() {
        return numVotes;
    }

    @JsonProperty
    public GenreModel[] getGenres() {
        return genres;
    }

    @JsonProperty
    public StarModelForMovies[] getStars() {
        return stars;
    }


    public Boolean isHidden() { return hidden; }
}
