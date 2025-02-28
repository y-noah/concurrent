package com.example.concurrentserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Test {
    private int id;
    private String name;
    private int age;
    private String text;
}