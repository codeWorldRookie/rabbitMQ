package com.caotf.rabbitmq.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {
   private static final String EXCHANGE_NAME = "direct_logs";

   public static void main(String[] args) throws IOException, TimeoutException {
      String messages[][] = new String[][] {
         {"waring.log1", "waring message1"},
         {"waring.log1", "waring message2"},
         {"error.log1", "error message"},
         {"error.log2", "error message"},
      };
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();
      //channel.exchangeDeclare(EXCHANGE_NAME, "topic");

      for(int i = 0; i < messages.length; i++) {
         String severity = messages[i][0];
         String message = messages[i][1];
         channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
         System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
      }
   }
}
