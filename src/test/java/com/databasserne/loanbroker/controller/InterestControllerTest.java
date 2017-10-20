package com.databasserne.loanbroker.controller;

import com.rabbitmq.client.*;
import org.junit.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InterestControllerTest {

    private InterestController interestController;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private Channel replyChannel;
    private AMQP.Queue.DeclareOk declareOk;
    private DefaultConsumer consumer;

    public InterestControllerTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {
        factory = mock(ConnectionFactory.class);
        connection = mock(Connection.class);
        channel = mock(Channel.class);
        replyChannel = mock(Channel.class);
        declareOk = mock(AMQP.Queue.DeclareOk.class);
        consumer = mock(DefaultConsumer.class);
        this.interestController = new InterestController(factory);

    }

    @After
    public void tearDown() {}

    @Test
    public void test() {

    }

    /*@Test
    public void getInterestSuccessTest() throws IOException, TimeoutException {
        when(factory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);
        when(channel.queueDeclare()).thenReturn(declareOk);
        when(declareOk.getQueue()).thenReturn("test-queue");

        String response = interestController.getInterest("12345678", 10000, 12);
        assertThat(response, is(notNullValue()));
    }*/

}
