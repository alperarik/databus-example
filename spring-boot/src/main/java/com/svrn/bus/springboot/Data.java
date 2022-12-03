package com.databus.springboot;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Data {

    @NotNull
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String data;

    private Long timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
