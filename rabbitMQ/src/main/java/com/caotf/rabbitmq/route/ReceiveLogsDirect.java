package com.caotf.rabbitmq.route;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect {
   private static final String EXCHANGE_NAME = "direct_logs";

   public static void main(String[] args) throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");
      String queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, EXCHANGE_NAME, "error");

      String queueName2 = channel.queueDeclare().getQueue();
      channel.queueBind(queueName2, EXCHANGE_NAME, "waring");

      DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
         String message = new String(delivery.getBody(), "UTF-8");
         System.out.println("error:" +
                 delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };

      DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
         String message = new String(delivery.getBody(), "UTF-8");
         System.out.println("waring" +
                 delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };

      channel.basicConsume(queueName, true, deliverCallback1, consumerTag -> { });

      channel.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
   }
}
