package com.databus.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private KafkaService kafkaService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Data createCustomer(@RequestBody @Valid Data data) {
        data.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond());
        kafkaService.sendCustomerData(data);
        return data;
    }
}
