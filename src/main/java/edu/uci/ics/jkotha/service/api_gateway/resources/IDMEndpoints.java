package edu.uci.ics.jkotha.service.api_gateway.resources;

import edu.uci.ics.jkotha.service.api_gateway.GatewayService;
import edu.uci.ics.jkotha.service.api_gateway.configs.IDMConfigs;
import edu.uci.ics.jkotha.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.jkotha.service.api_gateway.models.DefaultResponseModel;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.LoginRequestModel;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.PrivilegeRequestModel;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.RegisterRequestModel;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.SessionsRequestModel;
import edu.uci.ics.jkotha.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.jkotha.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.jkotha.service.api_gateway.threadpool.ThreadPool;
import edu.uci.ics.jkotha.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.jkotha.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("idm")
public class IDMEndpoints {
    private IDMConfigs idmConfigs = GatewayService.getIdmConfigs();
    private ThreadPool threadPool = GatewayService.getThreadPool();
    private int delay = GatewayService.getGatewayConfigs().getRequestDelay();

    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to register user.");
        RegisterRequestModel requestModel;

        try {
            requestModel = (RegisterRequestModel) ModelValidator.verifyModel(jsonText,RegisterRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, DefaultResponseModel.class);
        }

        //Transaction ID:
        String transactionId = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(idmConfigs.getIdmUri());
        cr.setEndpoint(idmConfigs.getEPUserRegister());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionId);
        cr.setMethod("post");
        cr.setEmail(requestModel.getEmail());

        threadPool.add(cr);


        return Response.status(Status.NO_CONTENT).header("transactionID", transactionId).header("delay", delay).build();
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUserRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to login user.");
        LoginRequestModel requestModel;

        try{
            requestModel = (LoginRequestModel)ModelValidator.verifyModel(jsonText,LoginRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, DefaultResponseModel.class);
        }

        //Transaction ID:
        String transactionId = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(idmConfigs.getIdmUri());
        cr.setEndpoint(idmConfigs.getEPUserLogin());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionId);
        cr.setMethod("post");
        cr.setEmail(requestModel.getEmail());

        threadPool.add(cr);

        return Response.status(Status.NO_CONTENT).header("transactionID", transactionId).header("delay", delay).build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySessionRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to verify session");
        SessionsRequestModel requestModel;

        try {
            requestModel = (SessionsRequestModel) ModelValidator.verifyModel(jsonText, SessionsRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        //Transaction ID:
        String transactionId = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(idmConfigs.getIdmUri());
        cr.setEndpoint(idmConfigs.getEPSessionVerify());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionId);
        cr.setMethod("post");
        cr.setEmail(requestModel.getEmail());
        cr.setSessionID(requestModel.getSessionID());

        threadPool.add(cr);

        return Response.status(Status.NO_CONTENT).header("transactionID", transactionId).header("delay", delay).build();
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserPrivilegeRequest(String jsonText, @Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Received request to get RequestModel");
        PrivilegeRequestModel requestModel;

        try{
            requestModel = (PrivilegeRequestModel) ModelValidator.verifyModel(jsonText,PrivilegeRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = requestModel.getEmail();
        String sessionId = headers.getHeaderString("sessionID");
        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        //Transaction ID:
        String transactionId = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(idmConfigs.getIdmUri());
        cr.setEndpoint(idmConfigs.getEPUserPrivilegeVerify());
        cr.setRequest(requestModel);
        cr.setTransactionID(transactionId);
        cr.setMethod("post");
        cr.setEmail(email);
        cr.setSessionID(headers.getHeaderString("sessionID"));

        threadPool.add(cr);

        return Response.status(Status.NO_CONTENT).header("transactionID", transactionId).header("delay", delay).build();
    }
}
