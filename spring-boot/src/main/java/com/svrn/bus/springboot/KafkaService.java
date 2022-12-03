package com.databus.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Logger logger = LoggerFactory.getLogger(DataController.class);

    private final static String dataTopicName = "data_topic";

    public void sendCustomerData(Data data) {

        try {
            String json = new ObjectMapper().writeValueAsString(data);
            kafkaTemplate.send(dataTopicName, data.getName(), json);
            logger.info(String.format("Message sent from REST to Kafka: %s", data.getName()));
        } catch (JsonProcessingException e) {
            logger.info(String.format("Error: when sending kafka message: %s", e.getMessage()));
        }

    }
}
