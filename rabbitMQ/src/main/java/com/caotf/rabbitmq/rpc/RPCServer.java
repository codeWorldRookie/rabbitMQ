package com.caotf.rabbitmq.rpc;

import com.caotf.rabbitmq.Util;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RPCServer {
   public static final String RPC_QUEUE = "rpc_queue";

   public static void main(String[] args) throws IOException, TimeoutException {
      Channel channel = Util.getChannel();
      channel.queueDeclare(RPC_QUEUE, true, false,false, null);
      channel.queuePurge(RPC_QUEUE);
      channel.basicQos(1);

      channel.basicConsume(RPC_QUEUE, false, ((consumerTag, message) -> {
         AMQP.BasicProperties replyProps = new AMQP.BasicProperties
            .Builder()
            .correlationId(message.getProperties().getCorrelationId())
            .build();
         System.out.println("收到消息");
         channel.basicPublish("", message.getProperties().getReplyTo(), replyProps,
            (new String(message.getBody()) + "***").getBytes());
         channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
      }), (consumerTag -> {}));
   }
}
