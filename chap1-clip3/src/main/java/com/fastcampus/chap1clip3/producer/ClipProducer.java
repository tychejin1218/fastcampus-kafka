package com.fastcampus.chap1clip3.producer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class ClipProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final RoutingKafkaTemplate routingKafkaTemplate;
  private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

  public ClipProducer(KafkaTemplate<String, String> kafkaTemplate,
      RoutingKafkaTemplate routingKafkaTemplate,
      ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
    this.routingKafkaTemplate = routingKafkaTemplate;
    this.replyingKafkaTemplate = replyingKafkaTemplate;
  }

  public void async(String topic, String message) {
    ListenableFuture<SendResult<String, String>> tufure = kafkaTemplate.send(topic, message);
    tufure.addCallback(new KafkaSendCallback<>() {

      @Override
      public void onSuccess(SendResult<String, String> result) {
        System.out.println("Success to send message.");
      }

      @Override
      public void onFailure(KafkaProducerException ex) {
        ProducerRecord<Object, Object> record = ex.getFailedProducerRecord();
        System.out.println("Fail to end message. record=" + record);
      }
    });
  }

  public void sync(String topic, String message) {
    ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
    try {
      System.out.println("Success to send sync message.");
      future.get(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
  }

  public void routingSend(String topic, String message) {
    routingKafkaTemplate.send(topic, message);
  }

  public void routingSendBytes(String topic, byte[] message) {
    routingKafkaTemplate.send(topic, message);
  }

  public void replyingSend(String topic, String message)
      throws ExecutionException, InterruptedException, TimeoutException {
    ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
    RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(
        record);
    ConsumerRecord<String, String> consumerRecord = replyFuture.get(10,
        TimeUnit.SECONDS);
    System.out.println(consumerRecord.value());
  }
}
