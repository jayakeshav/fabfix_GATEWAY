package edu.uci.ics.jkotha.service.api_gateway.resources;


import edu.uci.ics.jkotha.service.api_gateway.GatewayService;
import edu.uci.ics.jkotha.service.api_gateway.configs.MovieConfigs;
import edu.uci.ics.jkotha.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.jkotha.service.api_gateway.models.DefaultResponseModel;
import edu.uci.ics.jkotha.service.api_gateway.models.IdmModels.SessionResponseModel;
import edu.uci.ics.jkotha.service.api_gateway.models.MovieModels.*;
import edu.uci.ics.jkotha.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.jkotha.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.jkotha.service.api_gateway.threadpool.ThreadPool;
import edu.uci.ics.jkotha.service.api_gateway.utilities.CheckSession;
import edu.uci.ics.jkotha.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.jkotha.service.api_gateway.utilities.ResultCodes;
import edu.uci.ics.jkotha.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.Map;

@Path("movies")
public class MovieEndpoints {
    private MovieConfigs movieConfigs = GatewayService.getMovieConfigs();
    private ThreadPool threadPool = GatewayService.getThreadPool();
    private int delay = GatewayService.getGatewayConfigs().getRequestDelay();
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(
            @Context HttpHeaders headers,
            @QueryParam("title") String title,
            @QueryParam("genre") String genre,
            @QueryParam("year") int year,
            @QueryParam("director") String director,
            @QueryParam("hidden") boolean hidden,
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String sortby,
            @QueryParam("direction") String orderby
    ) {
        ServiceLogger.LOGGER.info("Received at Search Movies Ep.");

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        Map<String,Object> qpMap = new HashMap<>();
        if (title!=null)
            qpMap.put("title",title);
        if (genre!=null)
            qpMap.put("genre",genre);
        if (year !=0)
            qpMap.put("year",year);
        if (director != null)
            qpMap.put("director",director);
        if (hidden)
            qpMap.put("hidden",hidden);

        if (limit != 25 && limit != 10 && limit != 100 && limit != 50)
            limit = 10;
        if (offset < 0 || offset % limit != 0)
            offset = 0;

        qpMap.put("offset",offset);
        qpMap.put("limit",limit);
        qpMap.put("orderby",sortby);
        qpMap.put("direction",orderby);

        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPMovieSearch());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("get");
        cr.setQueryParamValues(qpMap);

        ServiceLogger.LOGGER.info(email);

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }
        
        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("email", email).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieRequest(
            @PathParam("movieid") String id,
            @Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Received at Get Movie Ep.");

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        Map<String,Object> ppMap = new HashMap<>();
        ppMap.put("movieid",id);

        String transactionID = TransactionIDGenerator.generateTransactionID();


        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPMovieGet());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("get");
        cr.setPathParam(ppMap);

        ServiceLogger.LOGGER.info(email);

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }
        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("email", email).header("sessionID", sessionId).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(
            String jsonText,
        @Context HttpHeaders headers
    ) {
        ServiceLogger.LOGGER.info("Received at Add Movie Ep.");
        AddRequestModel requestModel;

        try {
            requestModel = (AddRequestModel) ModelValidator.verifyModel(jsonText,AddRequestModel.class);
        }catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPMovieAdd());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setMethod("post");

        ServiceLogger.LOGGER.info(email);

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(
            @PathParam("movieid") String id,
            @Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Received at Delete Movie Ep.");

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        Map<String,Object> ppMap = new HashMap<>();
        ppMap.put("movieid",id);

        String transactionID = TransactionIDGenerator.generateTransactionID();


        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPMovieDelete());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("delete");
        cr.setPathParam(ppMap);

        ServiceLogger.LOGGER.info(email);

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("genre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresRequest(
            @Context HttpHeaders headers
    ) {
        ServiceLogger.LOGGER.info("Received at Genre Ep.");

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

//        //verify session is given
//        if (sessionId == null) {
//            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
//        } else if (sessionId.length() == 0) {
//            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
//        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPGenreGet());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("get");
//
//        cr = CheckSession.verifySessionResponse(cr);
//        if (cr.isSessionExpired()) {
//            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
//            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
//        }

        threadPool.add(cr);

//        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Expose-Headers", "*").header("Access-Control-Allow-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("genre/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(
            @Context HttpHeaders headers,String jsonText
    ) {
        ServiceLogger.LOGGER.info("Received at Add Genre Ep.");
        GenreAddRequestModel requestModel;

        try {
            requestModel = (GenreAddRequestModel) ModelValidator.verifyModel(jsonText,GenreAddRequestModel.class);
        }catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPGenreAdd());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setMethod("post");

        ServiceLogger.LOGGER.info(email);

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("genre/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMovieRequest(
            @PathParam("movieid") String id,
            @Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Received at Get Movie Genre Ep.");

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        Map<String,Object> ppMap = new HashMap<>();
        ppMap.put("movieid",id);

        String transactionID = TransactionIDGenerator.generateTransactionID();

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }


        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPGenreMovie());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("get");
        cr.setPathParam(ppMap);

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("X-Content-Type-Options", "nosniff").header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("star/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starSearchRequest(
            @Context HttpHeaders headers,
            @QueryParam("name") String name,
            @QueryParam("movieTitle") String movieTitle,
            @QueryParam("birthYear") int year,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset,
            @QueryParam("orderby") String sortby,
            @QueryParam("direction") String orderby
    ) {
        ServiceLogger.LOGGER.info("Received at Star Search Ep.");

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        Map<String,Object> qpMap = new HashMap<>();
        if (name!=null)
            qpMap.put("name",name);
        if (movieTitle!=null)
            qpMap.put("movieTitle",movieTitle);
        if (year !=0)
            qpMap.put("birthYear",year);
        if (limit != 25 && limit != 10 && limit != 100 && limit != 50)
            limit = 10;
        if (offset < 0 || offset % limit != 0)
            offset = 0;

        qpMap.put("offset",offset);
        qpMap.put("limit",limit);
        qpMap.put("orderby",sortby);
        qpMap.put("direction",orderby);

        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPStarSearch());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("get");
        cr.setQueryParamValues(qpMap);

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("star/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(
            @Context HttpHeaders headers,
            @PathParam("id") String id
    ) {
        ServiceLogger.LOGGER.info("Received at Get Star Ep.");

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        Map<String,Object> ppMap = new HashMap<>();
        ppMap.put("id",id);

        String transactionID = TransactionIDGenerator.generateTransactionID();


        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPStarGet());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setMethod("get");
        cr.setPathParam(ppMap);

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("star/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(
            @Context HttpHeaders headers, String jsonText
    ) {
        StarAddRequestModel requestModel;
        ServiceLogger.LOGGER.info("Received at Add Star Ep.");

        try {
            requestModel = (StarAddRequestModel) ModelValidator.verifyModel(jsonText,StarAddRequestModel.class);
        }catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPStarAdd());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setMethod("post");

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("star/starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovieRequest(
            @Context HttpHeaders headers,String jsonText
    ) {
        StarAddMovieRequestModel requestModel;
        ServiceLogger.LOGGER.info("Received at Add Star Ep.");

        try {
            requestModel = (StarAddMovieRequestModel) ModelValidator.verifyModel(jsonText,StarAddMovieRequestModel.class);
        }catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPStarIn());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setMethod("post");

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(
            @Context HttpHeaders headers,String jsonText
    ) {
        RatingsReqModel requestModel;
        ServiceLogger.LOGGER.info("Received at Ratings update Ep.");

        try {
            requestModel = (RatingsReqModel) ModelValidator.verifyModel(jsonText,RatingsReqModel.class);
        }catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e,DefaultResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        //verify session is given
        if (sessionId == null) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        } else if (sessionId.length() == 0) {
            return Response.status(Status.BAD_REQUEST).header("email", email).entity(new DefaultResponseModel(-17)).build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(movieConfigs.getMoviesUri());
        cr.setEndpoint(movieConfigs.getEPRating());
        cr.setEmail(email);
        cr.setSessionID(sessionId);
        cr.setTransactionID(transactionID);
        cr.setRequest(requestModel);
        cr.setMethod("post");

        cr = CheckSession.verifySessionResponse(cr);
        if (cr.isSessionExpired()) {
            SessionResponseModel responseModel = new SessionResponseModel(cr.getResultCode(), ResultCodes.setMessage(cr.getResultCode()));
            return Response.status(Status.BAD_REQUEST).header("email", email).header("sessionID", sessionId).entity(responseModel).build();
        }

        threadPool.add(cr);

        sessionId = cr.getSessionID();

        return Response.status(Status.NO_CONTENT).header("Access-Control-Allow-Headers", "*").header("Access-Control-Expose-Headers", "*").header("email", email).header("sessionID", sessionId).header("transactionID", transactionID).header("delay", delay).build();
    }
}
