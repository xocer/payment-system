package com.grishin.individuals.api;

import org.springframework.boot.SpringApplication;

public class TestIndividualsApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(IndividualsApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
