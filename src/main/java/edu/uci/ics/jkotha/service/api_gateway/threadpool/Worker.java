package edu.uci.ics.jkotha.service.api_gateway.threadpool;

import edu.uci.ics.jkotha.service.api_gateway.GatewayService;
import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.jkotha.service.api_gateway.models.RequestModel;
import org.glassfish.jersey.internal.util.ExceptionUtils;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

public class Worker extends Thread {
    private int id;
    private ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id=id;
        this.threadPool=threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        Worker worker = new  Worker(id,threadPool);
        return worker;
    }

    public void process(ClientRequest request) {
        try {
            if (request.getMethod().equalsIgnoreCase("post")) {

                Client client = ClientBuilder.newClient();
                client.register(JacksonFeature.class);

                WebTarget webTarget = client.target(request.getURI()).path(request.getEndpoint());

                Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

                RequestModel rm = request.getRequest();

                builder.header("email", request.getEmail());
                builder.header("sessionID", request.getSessionID());
                builder.header("transactionID", request.getTransactionID());

                Response response;

                response = builder.post(Entity.entity(rm, MediaType.APPLICATION_JSON));

                ServiceLogger.LOGGER.info("data received from server,putting it into DB by worker:" + id);

                insertIntoDatabase(response, request);

                ServiceLogger.LOGGER.info("request processed and connection returned");

                return;
            } else if (request.getMethod().equalsIgnoreCase("get")) {

                if (request.getPathParam() == null & request.getQueryParamValues() != null)//has query params
                {
                    Client client = ClientBuilder.newClient();
                    client.register(JacksonFeature.class);

                    WebTarget webTarget = client.target(request.getURI()).path(request.getEndpoint());

                    Iterator iterator = request.getQueryParamValues().entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry pair = (Map.Entry) iterator.next();
                        webTarget = webTarget.queryParam((String) pair.getKey(), pair.getValue());
                    }

                    ServiceLogger.LOGGER.info("Final Uri: " + webTarget.getUri().toString());

                    Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

                    builder.header("email", request.getEmail());
                    builder.header("sessionID", request.getSessionID());
                    builder.header("transactionID", request.getTransactionID());

                    builder.accept(MediaType.APPLICATION_JSON);

                    Response response = builder.get();

                    ServiceLogger.LOGGER.info("data received from server,putting it into DB by worker:" + id);

                    //database insert
                    insertIntoDatabase(response, request);

                } else if (request.getPathParam() != null)//has path param
                {
                    Client client = ClientBuilder.newClient();
                    client.register(JacksonFeature.class);

                    WebTarget webTarget = client.target(request.getURI()).path(request.getEndpoint());

                    webTarget = webTarget.resolveTemplates(request.getPathParam());

                    ServiceLogger.LOGGER.info("Final Uri: " + webTarget.getUri().toString());

                    Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

                    builder.header("email", request.getEmail());
                    builder.header("sessionID", request.getSessionID());
                    builder.header("transactionID", request.getTransactionID());

                    builder.accept(MediaType.APPLICATION_JSON);

                    Response response = builder.get();

                    ServiceLogger.LOGGER.info("data received from server,putting it into DB by worker:" + id);

                    //database insert
                    insertIntoDatabase(response, request);
                } else {
                    Client client = ClientBuilder.newClient();
                    client.register(JacksonFeature.class);

                    WebTarget webTarget = client.target(request.getURI()).path(request.getEndpoint());

                    ServiceLogger.LOGGER.info("Final Uri: " + webTarget.toString());

                    Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

                    builder.header("email", request.getEmail());
                    builder.header("sessionID", request.getSessionID());
                    builder.header("transactionID", request.getTransactionID());

                    builder.accept(MediaType.APPLICATION_JSON);

                    Response response = builder.get();

                    ServiceLogger.LOGGER.info("data received from server,putting it into DB by worker:" + id);

                    //database insert
                    insertIntoDatabase(response, request);
                }

                return;
            } else if (request.getMethod().equalsIgnoreCase("delete")) {
                Client client = ClientBuilder.newClient();
                client.register(JacksonFeature.class);

                WebTarget webTarget = client.target(request.getURI()).path(request.getEndpoint());

                webTarget = webTarget.resolveTemplates(request.getPathParam());

                ServiceLogger.LOGGER.info("Final Uri: " + webTarget.toString());

                Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

                builder.header("email", request.getEmail());
                builder.header("sessionID", request.getSessionID());
                builder.header("transactionID", request.getTransactionID());

                builder.accept(MediaType.APPLICATION_JSON);

                Response response = builder.delete();

                ServiceLogger.LOGGER.info("data received from server,putting it into DB by worker:" + id);

                //database insert
                insertIntoDatabase(response, request);

                return;
            }
        } catch (Exception e) {
            ServiceLogger.LOGGER.severe("thread fell into exception");
            ServiceLogger.LOGGER.severe(ExceptionUtils.exceptionStackTraceAsString(e));
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            insertIntoDatabase(response, request);
        }
        ServiceLogger.LOGGER.severe("thread reached end something wrong happened");
    }

    @Override
    public void run() {
        ServiceLogger.LOGGER.info("thread:"+id+" started!!!");
        while (true) {
            ClientRequest request = threadPool.remove();
            process(request);
            ServiceLogger.LOGGER.info("thread:"+id+" -Finished its task moving on !!");
        }
    }

    private void insertIntoDatabase(Response response, ClientRequest request){
        Connection con = GatewayService.getConPool().requestCon();
        String statement = "insert into responses(transactionid, email, sessionid, response, httpstatus) values (?,?,?,?,?)";
        try {
            PreparedStatement query = con.prepareStatement(statement);
            query.setString(1,request.getTransactionID());
            query.setString(2,request.getEmail());
            query.setString(3,request.getSessionID());
            query.setString(4,response.readEntity(String.class));
            query.setInt(5,response.getStatus());
            query.execute();
        }
        catch (SQLException e){
            ServiceLogger.LOGGER.warning(ExceptionUtils.exceptionStackTraceAsString(e));
        }
        GatewayService.getConPool().releaseCon(con);
    }
}
