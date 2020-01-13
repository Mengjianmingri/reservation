package com.itheima.pojo;

import redis.clients.jedis.Tuple;

import java.io.Serializable;

/**
 * 我的元类
 */
public class MyTuple implements Serializable {
    private String name;
    private Double value;

    public MyTuple() {
    }
    public MyTuple(Tuple tuple) {
        this.name = tuple.getElement();
        this.value = tuple.getScore();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MyTuple{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
