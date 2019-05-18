package edu.uci.ics.jkotha.service.api_gateway.models.BillingModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartInsertUpdateReqModel extends RequestModel {

    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private int quantity;

    @JsonCreator
    public CartInsertUpdateReqModel() {
    }


    @JsonCreator
    public CartInsertUpdateReqModel(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "movieId", required = true) String movieId,
            @JsonProperty(value = "quantity", required = true) int quantity) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public String getMovieId() {
        return movieId;
    }

    @JsonProperty
    public int getQuantity() {
        return quantity;
    }
}
