package edu.uci.ics.jkotha.service.api_gateway.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jkotha.service.api_gateway.GatewayService;
import edu.uci.ics.jkotha.service.api_gateway.configs.IDMConfigs;
import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.SessionResponseModel;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.SessionsRequestModel;
import edu.uci.ics.jkotha.service.api_gateway.threadpool.ClientRequest;
import org.glassfish.jersey.internal.util.ExceptionUtils;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class CheckSession {
    private static SessionResponseModel verifySessionResponseInternal(ClientRequest request) {
        if (request.getURI().equalsIgnoreCase("/register") || request.getURI().equalsIgnoreCase("/login"))
            return null;

        ServiceLogger.LOGGER.info("verifying if session is active");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        IDMConfigs configs = GatewayService.getIdmConfigs();

        WebTarget webTarget = client.target(configs.getIdmUri()).path(configs.getEPSessionVerify());

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
            return mapper.readValue(jsonText, SessionResponseModel.class);
        }catch (IOException e){
            ServiceLogger.LOGGER.warning(ExceptionUtils.exceptionStackTraceAsString(e));
            return null;
        }
    }

    public static ClientRequest verifySessionResponse(ClientRequest clientRequest) {
        SessionResponseModel model = verifySessionResponseInternal(clientRequest);

        if (model == null)
            return null;

        if (model.getResultCode() == 130) {
            clientRequest.setSessionExpired(false);
            return clientRequest;
        } else if (model.getResultCode() == 133) {
            clientRequest.setSessionExpired(false);
            clientRequest.setSessionID(model.getSessionID());
            return clientRequest;
        } else {
            clientRequest.setSessionExpired(true);
            clientRequest.setResultCode(model.getResultCode());
            return clientRequest;
        }
    }
}
