package com.databasserne.loanbroker.rest;

import com.databasserne.loanbroker.controller.InterestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.ConnectionFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/interest")
public class InteRest {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private InterestController ctrl;

    public InteRest() {
        this.ctrl = new InterestController(new ConnectionFactory());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClichedMessage(String content) {
        JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
        JsonObject response = new JsonObject();
        if(!inputJson.has("SSN") || !inputJson.has("Amount") || !inputJson.has("Duration")) {
            return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(gson.toJson(ctrl.getInterest(
                        inputJson.get("SSN").getAsString(),
                        inputJson.get("Amount").getAsInt(),
                        inputJson.get("Duration").getAsInt())))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
