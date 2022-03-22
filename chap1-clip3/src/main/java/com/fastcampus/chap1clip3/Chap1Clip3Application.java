package com.fastcampus.chap1clip3;

import com.fastcampus.chap1clip3.producer.ClipProducer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class Chap1Clip3Application {

  public static void main(String[] args) {
    SpringApplication.run(Chap1Clip3Application.class, args);
  }

  /*@Bean
  public ApplicationRunner runner(KafkaTemplate<String, String> kafkaTemplate) {
    return args -> {
      kafkaTemplate.send("clip3", "Hello, Clip3");
    };
  }*/

  @Bean
  public ApplicationRunner runner(ClipProducer clipProducer) {
    return args -> {
      clipProducer.async("clip3", "Hello, Clip3-async.");
      clipProducer.sync("clip3", "Hello, Clip3-sync.");
      clipProducer.routingSend("clip3", "Hello, Clip3-routing.");
      clipProducer.routingSendBytes("clip3-bytes", "Hello, Clip3-bytes.".getBytes(StandardCharsets.UTF_8));
      clipProducer.replyingSend("clip3-request", "Ping Clip3.");
    };
  }
}
