package com.dudu.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Properties;


@Service
public class OrderConsumer extends Thread {
    private static final Logger logger = LogManager.getLogger(OrderConsumer.class);

    private static long poolTimeOut = 1000;
    private KafkaConsumer<String, String> kafkaConsumer;
    private ActionHandler actionHandler;
    private TaskExecutor taskExecutor;

    public OrderConsumer(@Value("${kafka.orders.server}") String server,
                         @Value("${kafka.orders.groupId}") String groupId,
                         @Value("${kafka.orders.topic}") String topic,
                         DataSource dataSource) {
        Properties props = new Properties();
        props.put("bootstrap.servers", server);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        start();

        actionHandler = new ActionHandler(dataSource);
        taskExecutor = new SimpleAsyncTaskExecutor();
    }

    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(poolTimeOut);
            for (ConsumerRecord record : records) {
                taskExecutor.execute(() -> actionHandler.handle(record.key().toString(), record.value().toString()));
            }
        }
    }

}
