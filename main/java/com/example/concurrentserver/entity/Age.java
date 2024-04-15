package com.example.concurrentserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Age {
    private int id;
    private int min;
    private int max;
    private String type;
}
