package edu.uci.ics.jkotha.service.api_gateway.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.SessionResponseModel;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.SessionsRequestModel;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;
import edu.uci.ics.jkotha.service.api_gateway.threadpool.ClientRequest;
import org.glassfish.jersey.internal.util.ExceptionUtils;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class CheckSession {
    public static Wrapper verifySessionResponse(ClientRequest request){
        if (request.getURI().equalsIgnoreCase("/register") || request.getURI().equalsIgnoreCase("/login"))
            return null;

        ServiceLogger.LOGGER.info("verifying if session is active");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        WebTarget webTarget = client.target(request.getURI()).path(request.getEndpoint());

        Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

        SessionsRequestModel rm = new SessionsRequestModel(request.getEmail(),request.getSessionID());

        builder.header("email",request.getEmail());
        builder.header("sessionID",request.getSessionID());
        builder.header("transactionID",request.getTransactionID());

        Response response ;

        response=builder.post(Entity.entity(rm,MediaType.APPLICATION_JSON));

        String jsonText = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new Wrapper(mapper.readValue(jsonText,SessionResponseModel.class), response);
        }catch (IOException e){
            ServiceLogger.LOGGER.warning(ExceptionUtils.exceptionStackTraceAsString(e));
            return null;
        }
    }
}
