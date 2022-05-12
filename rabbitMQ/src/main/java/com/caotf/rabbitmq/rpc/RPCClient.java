package com.caotf.rabbitmq.rpc;

import com.caotf.rabbitmq.Util;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;

public class RPCClient {
   public static final String RPC_QUEUE = "rpc_queue";

   public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
      Channel channel = Util.getChannel();
      String queueName = channel.queueDeclare().getQueue();
      String id = UUID.randomUUID().toString();
      BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

      String ctag = channel.basicConsume(queueName, true, (consumerTag, message) ->  {
         System.out.println("收到");
         if(Objects.equals(message.getProperties().getCorrelationId(), id)) {
            System.out.println("是等待的结果");
            blockingQueue.offer(new String(message.getBody()));
         }
      }, (tag) -> {});

      AMQP.BasicProperties properties = new AMQP.BasicProperties()
         .builder()
         .correlationId(id)
         .replyTo(queueName)
         .build();

      channel.basicPublish("", RPC_QUEUE, true, false, properties, "parameter".getBytes());
      System.out.println("发布成功");
      System.out.println("计算结果为" + blockingQueue.take());
      channel.basicCancel(ctag);
   }
}
