package edu.uci.ics.jkotha.service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.jkotha.service.api_gateway.GatewayService;

public class NoRequestResponseModel {
    private int resultCode;
    private String message;
    private int delay;

    public NoRequestResponseModel() {
        this.resultCode = 410;
        this.message = "No request to retrieve";
        this.delay = GatewayService.getGatewayConfigs().getRequestDelay();
    }

    @JsonProperty(value = "resultCode",required = true)
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message",required = true)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "delay",required = true)
    public int getDelay() {
        return delay;
    }
}
