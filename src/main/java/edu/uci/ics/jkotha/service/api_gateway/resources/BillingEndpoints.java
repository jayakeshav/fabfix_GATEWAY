package edu.uci.ics.jkotha.service.api_gateway.resources;

import edu.uci.ics.jkotha.service.api_gateway.GatewayService;
import edu.uci.ics.jkotha.service.api_gateway.configs.BillingConfigs;
import edu.uci.ics.jkotha.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.jkotha.service.api_gateway.models.BillingModels.*;
import edu.uci.ics.jkotha.service.api_gateway.models.DefaultResponseModel;
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

@Path("billing")
public class BillingEndpoints {
    private BillingConfigs billingConfigs = GatewayService.getBillingConfigs();
    private ThreadPool threadPool = GatewayService.getThreadPool();
    private int delay = GatewayService.getGatewayConfigs().getRequestDelay();

    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Insert in Cart");
        CartInsertUpdateReqModel requestModel;

        try {
            requestModel = (CartInsertUpdateReqModel) ModelValidator.verifyModel(jsonText,CartInsertUpdateReqModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCartInsert());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Update Cart");
        CartInsertUpdateReqModel requestModel;

        try {
            requestModel = (CartInsertUpdateReqModel) ModelValidator.verifyModel(jsonText,CartInsertUpdateReqModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCartUpdate());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Delete Cart");
        CartDeleteReqModel requestModel;

        try {
            requestModel = (CartDeleteReqModel) ModelValidator.verifyModel(jsonText,CartDeleteReqModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCartDelete());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Retrieve Cart");
        JustEmailReqModel requestModel;

        try {
            requestModel = (JustEmailReqModel) ModelValidator.verifyModel(jsonText,JustEmailReqModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCartRetrieve());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Clear Cart");
        CartDeleteReqModel requestModel;

        try {
            requestModel = (CartDeleteReqModel) ModelValidator.verifyModel(jsonText,CartDeleteReqModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCartClear());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("creditcard/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Insert CC");
        CreditCardModel requestModel;

        try {
            requestModel = (CreditCardModel) ModelValidator.verifyModel(jsonText,CreditCardModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCcInsert());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("creditcard/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Update CC");
        CreditCardModel requestModel;

        try {
            requestModel = (CreditCardModel) ModelValidator.verifyModel(jsonText,CreditCardModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCcUpdate());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("creditcard/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Delete CC");
        JustId requestModel;

        try {
            requestModel = (JustId) ModelValidator.verifyModel(jsonText,JustId.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCcDelete());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("creditcard/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to retrieve CC");
        JustId requestModel;

        try {
            requestModel = (JustId) ModelValidator.verifyModel(jsonText,JustId.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCcRetrieve());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("customer/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Insert Customer");
        CustomerModel requestModel;

        try {
            requestModel = (CustomerModel) ModelValidator.verifyModel(jsonText,CustomerModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCustomerInsert());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("customer/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Update Customer");
        CustomerModel requestModel;

        try {
            requestModel = (CustomerModel) ModelValidator.verifyModel(jsonText,CustomerModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCustomerUpdate());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("customer/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Retrieve Customer");
        JustEmailReqModel requestModel;

        try {
            requestModel = (JustEmailReqModel) ModelValidator.verifyModel(jsonText,JustEmailReqModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPCustomerRetrieve());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Place Order");
        JustEmailReqModel requestModel;

        try {
            requestModel = (JustEmailReqModel) ModelValidator.verifyModel(jsonText,JustEmailReqModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPOrderPlace());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }

    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to Place Order");
        JustEmailReqModel requestModel;

        try {
            requestModel = (JustEmailReqModel) ModelValidator.verifyModel(jsonText,JustEmailReqModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //Transaction ID:
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(billingConfigs.getBillingUri());
        cr.setEndpoint(billingConfigs.getEPOrderRetrieve());
        cr.setRequest(requestModel);
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("post");

        threadPool.add(cr);

        NoContentResponseModel responseModel = new NoContentResponseModel(delay,transactionID);

        return Response.status(Status.NO_CONTENT).header("transactionId",transactionID).entity(responseModel).build();
    }
}
