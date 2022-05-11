package com.caotf.rabbitmq.worker;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {
   public static final String QUEUE_NAME = "hello";

   public static void main(String[] args) throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");

      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();
      channel.queueDeclare(QUEUE_NAME, true, false, false, null);
      //channel.basicQos(1);

      for(int i = 0; i < 5; i++) {
         channel.basicPublish("", QUEUE_NAME, null, ("hello world" + i).getBytes());
         //MessageProperties.PERSISTENT_TEXT_PLAIN
         System.out.println("发布成功");
      }

      connection.close();
   }
}
