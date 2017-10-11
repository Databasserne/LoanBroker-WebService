package com.databasserne.loanbroker.controller;

import com.google.gson.JsonObject;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class InterestController {

    private static final String QUEUE_NAME = "";

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private Consumer consumer;

    String response = null;
    boolean isWaiting = true;

    public static void main(String[] args) {
        System.out.println(new InterestController().getInterest("1234", 10000, 2));

    }

    public String getInterest(String ssn, int amount, int duration) {
        factory = new ConnectionFactory();
        factory.setHost("datdb.cphbusiness.dk");

        JsonObject json = new JsonObject();
        json.addProperty("SSN", ssn);
        json.addProperty("Months", duration);
        json.addProperty("Amount", amount);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String reply = channel.queueDeclare().getQueue();
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .replyTo(reply)
                    .build();

            channel.basicPublish("", QUEUE_NAME, props, json.toString().getBytes());
            Channel replyChannel = connection.createChannel();
            consumer = new DefaultConsumer(replyChannel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    response = new String(body, "UTF-8");
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
