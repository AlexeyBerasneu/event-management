package com.alexber.eventmanager;

import org.springframework.boot.SpringApplication;

public class TestEventManagerApplication {

    public static void main(String[] args) {
        SpringApplication.from(EventManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
