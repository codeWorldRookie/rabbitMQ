package com.caotf.rabbitmq.publish_subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLog {
   private static final String EXECHANGE_NAME = "logs";

   public static void main(String[] args) throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");

      Connection connection = factory.newConnection();
      //channel.exchangeDeclare(EXECHANGE_NAME, "fanout");

      for(int i = 0; i < 5; i++) {
         Channel channel = connection.createChannel();
         channel.basicPublish(EXECHANGE_NAME, "22", null, ("log" + i).getBytes());
         System.out.println("发布日志成功");
      }

      connection.close();
   }
}
