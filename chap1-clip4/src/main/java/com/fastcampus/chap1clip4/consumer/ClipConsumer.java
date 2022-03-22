package com.fastcampus.chap1clip4.consumer;


import com.fastcampus.chap1clip4.model.Animal;
import java.util.Date;
import javax.validation.Valid;
import javax.xml.crypto.Data;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class ClipConsumer {

  /*@KafkaListener(id = "clip4-listener-id", topics = "clip4-listener", concurrency = "2", clientIdPrefix = "listener-id")
  public void listen(String message) {
    System.out.println("Listener. message=" + message);
  }*/

  @KafkaListener(id = "clip4-listener-id", topics = "clip4-listener")
  public void listen(String message,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
      @Header(KafkaHeaders.OFFSET) long offset,
      ConsumerRecordMetadata metadata) {
    System.out.println("Listener. offset" + metadata.offset() +
        ", partition=" + partition +
        ", timestamp=" + new Date(timestamp) +
        ", message=" + message);
  }

  @KafkaListener(id = "clip4-animal-listener", topics = "clip4-animal", containerFactory = "kafkaJsonContainerFactory")
  public void listenAnimal(@Valid Animal animal) {
    System.out.println("Animal. animal=" + animal);
  }
}
