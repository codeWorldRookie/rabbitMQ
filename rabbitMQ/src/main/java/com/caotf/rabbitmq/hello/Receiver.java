package com.caotf.rabbitmq.hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Receiver {
   public static void main(String[] args) throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");

      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();
      channel.queueDeclare(Sender.QUEUE_NAME, true, false, false, null);
      System.out.println("等待消息");
      DeliverCallback deliverCallback = (consumerTag, message) -> {
         System.out.println("收到消息: " + new String(message.getBody(), StandardCharsets.UTF_8));
      };

      channel.basicConsume(Sender.QUEUE_NAME, true, deliverCallback, consumerTag -> { });
   }
}
