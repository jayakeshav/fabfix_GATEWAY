package edu.uci.ics.jkotha.service.api_gateway.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jkotha.service.api_gateway.GatewayService;
import edu.uci.ics.jkotha.service.api_gateway.configs.IDMConfigs;
import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.jkotha.service.api_gateway.models.DefaultResponseModel;
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
        if (response.getStatus() != -1) {
            try {
                return mapper.readValue(jsonText, SessionResponseModel.class);
            }catch (IOException e){
                try {
                    DefaultResponseModel res = mapper.readValue(jsonText, DefaultResponseModel.class);
                    return new SessionResponseModel(res.getResultCode(), res.getMessage());
                } catch (IOException e1) {
                    ServiceLogger.LOGGER.warning(ExceptionUtils.exceptionStackTraceAsString(e1));
                    return null;
                }
            }
        } else
            return null;
    }

    public static ClientRequest verifySessionResponse(ClientRequest clientRequest) {
        SessionResponseModel model = verifySessionResponseInternal(clientRequest);
        ServiceLogger.LOGGER.info("Session Id:" + clientRequest.getSessionID());
        ServiceLogger.LOGGER.info("Session Id received:" + model.getSessionID());
        if (model == null) {
            ServiceLogger.LOGGER.info("screwed!!!!");
            clientRequest.setSessionExpired(true);
            return clientRequest;
        }
        if (model.getResultCode() == 130) {
            clientRequest.setSessionID(model.getSessionID());
            ServiceLogger.LOGGER.info("session is Active");
            clientRequest.setSessionExpired(false);
            return clientRequest;
        } else if (model.getResultCode() == 133) {
            ServiceLogger.LOGGER.info("session Revoked");
            clientRequest.setSessionExpired(true);
            clientRequest.setResultCode(model.getResultCode());
            return clientRequest;
        } else {
            ServiceLogger.LOGGER.info("session not active /screwed: result code:" + model.getResultCode());
            clientRequest.setSessionExpired(true);
            clientRequest.setResultCode(model.getResultCode());
            return clientRequest;
        }
    }
}
