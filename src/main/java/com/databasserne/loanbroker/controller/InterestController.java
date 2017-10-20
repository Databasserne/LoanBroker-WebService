package com.databasserne.loanbroker.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class InterestController {

    private static final String QUEUE_NAME = "";

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private Channel replyChannel;
    private Consumer consumer;

    JsonObject response = null;
    boolean isWaiting = true;

    public InterestController(ConnectionFactory factory) {
        this.factory = factory;
    }

    public JsonObject getInterest(String ssn, int amount, int duration) {
        factory.setHost("datdb.cphbusiness.dk");

        JsonObject json = new JsonObject();
        json.addProperty("SSN", ssn);
        json.addProperty("Months", duration);
        json.addProperty("Amount", amount);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            com.rabbitmq.client.AMQP.Queue.DeclareOk declareOk = channel.queueDeclare();
            String reply = declareOk.getQueue();
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .replyTo(reply)
                    .build();

            channel.basicPublish("", QUEUE_NAME, props, json.toString().getBytes());
            replyChannel = connection.createChannel();
            consumer = new DefaultConsumer(replyChannel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    String s = new String(body, "UTF-8");
                    response = new JsonParser().parse(s).getAsJsonObject();
                    isWaiting = false;
                }
            };

            while(isWaiting) {}

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException ex) {
            ex.printStackTrace();
        } finally {
            return response;
        }
    }
}
