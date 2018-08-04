package com.dudu.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Properties;


@Service
public class OrderConsumer extends Thread {
    private static final Logger logger = LogManager.getLogger(OrderConsumer.class);

    private static long poolTimeOut = 1000;
    private KafkaConsumer<String, String> kafkaConsumer;

    public OrderConsumer(@Value("${kafka.orders.server}") String server,
                         @Value("${kafka.orders.groupId}") String groupId,
                         @Value("${kafka.orders.topic}") String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", server);
        props.put("key.separator", ":");
        props.put("parse.key", true);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        start();
    }

    public void run() {
        logger.info("Comsumer start running");
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(poolTimeOut);
            for (ConsumerRecord record : records) {


                logger.info(record.key() + "=" + record.value());
            }
        }
    }

}
