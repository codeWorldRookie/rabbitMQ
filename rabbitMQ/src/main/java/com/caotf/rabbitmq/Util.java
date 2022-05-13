package com.caotf.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Util {
   public static Channel getChannel() throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
      Connection connection = factory.newConnection();
      return connection.createChannel();
   }

   public static Channel getChannel(Connection connection) throws IOException, TimeoutException {
      if(connection == null || !connection.isOpen()) {
         return null;
      }

      return connection.createChannel();
   }
}
