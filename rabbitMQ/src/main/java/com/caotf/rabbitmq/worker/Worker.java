package com.caotf.rabbitmq.worker;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Worker {
   public static void main(String[] args) throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");

      Connection connection = factory.newConnection();

      for(int i = 0; i < 2; i++) {
         Channel channel = connection.createChannel();
         channel.queueDeclare(Sender.QUEUE_NAME, true, false, false, null);
         System.out.println("等待消息");
         final int id = i;

         DeliverCallback deliverCallback = (consumerTag, message) -> {
            //System.out.println( id + "收到消息: " + new String(message.getBody(), StandardCharsets.UTF_8));
            try {
               int result = 1/id;
               Thread.sleep(2000);
               System.out.println( id + "处理完了: " + new String(message.getBody(), StandardCharsets.UTF_8));
               //channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
            catch(Exception e) {
               System.out.println(id + "处理失败" + new String(message.getBody(), StandardCharsets.UTF_8));
            }
         };

         channel.basicConsume(Sender.QUEUE_NAME, true, deliverCallback, consumerTag -> { });
      }
   }
}
