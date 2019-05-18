package edu.uci.ics.jkotha.service.api_gateway.models.BillingModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.models.ResponseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardRetrieveResponseModel extends ResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "creditcard")
    private CreditCardModel creditcard;


    @JsonCreator
    public CardRetrieveResponseModel(int resultCode, String message, CreditCardModel creditcard) {
        this.resultCode = resultCode;
        this.message = message;
        this.creditcard = creditcard;
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
    public CreditCardModel getCreditcard() {
        return creditcard;
    }
}
