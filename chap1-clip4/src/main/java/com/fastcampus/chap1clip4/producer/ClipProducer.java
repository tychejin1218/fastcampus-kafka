package com.fastcampus.chap1clip4.producer;

import com.fastcampus.chap1clip4.model.Animal;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class ClipProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaTemplate<String, Animal> kafkaJsonTemplate;

  public ClipProducer(KafkaTemplate<String, String> kafkaTemplate,
      KafkaTemplate<String, Animal> kafkaJsonTemplate) {
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaJsonTemplate = kafkaJsonTemplate;
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

  public void async(String topic, Animal animal) {
    kafkaJsonTemplate.send(topic, animal);
  }
}
