package com.joaoscioli.billing;

import org.springframework.boot.SpringApplication;

public class TestSubscriptionBillingApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(SubscriptionBillingApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
