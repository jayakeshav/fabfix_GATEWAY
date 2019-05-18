package edu.uci.ics.jkotha.service.api_gateway.models.BillingModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRetrieveResponseModel extends ResponseModel {

    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "items")
    private TransactionModel[] items;

    @JsonCreator
    public OrderRetrieveResponseModel(int resultCode, String message, TransactionModel[] items) {
        this.resultCode = resultCode;
        this.message = message;
        this.items = items;
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
    public TransactionModel[] getItems() {
        return items;
    }
}
