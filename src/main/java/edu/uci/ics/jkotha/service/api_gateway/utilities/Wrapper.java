package edu.uci.ics.jkotha.service.api_gateway.utilities;


import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.SessionResponseModel;

import javax.ws.rs.core.Response;

public class Wrapper {
    private SessionResponseModel model;
    private Response response;

    public Wrapper(SessionResponseModel model, Response response) {
        this.model = model;
        this.response = response;
    }

    public SessionResponseModel getModel() {
        return model;
    }

    public Response getResponse() {
        return response;
    }
}
