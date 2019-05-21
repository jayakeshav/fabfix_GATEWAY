package edu.uci.ics.jkotha.service.api_gateway.resources;

import edu.uci.ics.jkotha.service.api_gateway.GatewayService;
import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.jkotha.service.api_gateway.models.NoRequestResponseModel;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("report")
public class ReportEndpoint {
    private static int delay = GatewayService.getGatewayConfigs().getRequestDelay();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReport(@Context HttpHeaders headers){
        ServiceLogger.LOGGER.info("Received at Report Ep.");

        String email = headers.getHeaderString("email");
        String sessionId = headers.getHeaderString("sessionID");
        String transactionId = headers.getHeaderString("transactionID");

        String get = "select sessionid,response,httpstatus from responses where transactionid=?";
        try {
            Connection con = GatewayService.getConPool().requestCon();
            PreparedStatement ps = con.prepareStatement(get);
            ps.setString(1,transactionId);
            ResultSet rs= ps.executeQuery();
            if (rs.next()){
                String jsonText = rs.getString("response");
                int httpStatus = rs.getInt("httpstatus");
                sessionId = rs.getString("sessionid");
                GatewayService.getConPool().releaseCon(con);
                return Response.status(httpStatus).entity(jsonText).header("email",email).header("sessionId",sessionId).build();
            }
            else {
                GatewayService.getConPool().releaseCon(con);
                return Response.status(Response.Status.NO_CONTENT).header("email", email).header("sessionId", sessionId).header("transactionID", transactionId).header("delay", delay).build();
            }
        }catch (SQLException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
