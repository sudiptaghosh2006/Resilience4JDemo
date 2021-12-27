package com.sudipta.resilience4j.circuitbreaker;

import java.time.Duration;
import java.util.function.Supplier;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;

public class ResourceConsumerRunner
{

    public static void main(String[] args) throws InterruptedException
    {

	// Circuit Breaker Config
	CircuitBreakerConfig config = CircuitBreakerConfig.custom().slidingWindow(3, 2, SlidingWindowType.COUNT_BASED)
		.automaticTransitionFromOpenToHalfOpenEnabled(true).failureRateThreshold(50).permittedNumberOfCallsInHalfOpenState(3)
		.maxWaitDurationInHalfOpenState(Duration.ofSeconds(5)).build();

	// Circuit Breaker registry

	CircuitBreakerRegistry registry = CircuitBreakerRegistry.custom().withCircuitBreakerConfig(config).build();

	// Circuit Breaker

//	CircuitBreaker circuitBreaker = CircuitBreaker.of("MyCirCuitBreaker", config);

	CircuitBreaker circuitBreaker = registry.circuitBreaker("MyCirCuitBreaker");
	// supplier

	ApplicationServiceProvider applicationResources = new ApplicationServiceProvider();

	Supplier<String> decorateSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> applicationResources.getResource());

	// call the method

	for (int i = 0; i < 3; i++)
	{
	    System.out.println("***************************************************************");
	    System.out.println("Current State before call ===>>> " + circuitBreaker.getState());
	    Try.ofSupplier(decorateSupplier).recover(applicationResources::getFallbackResource);
	    System.out.println("Current State after call ===>>> " + circuitBreaker.getState());
	    System.out.println("***************************************************************");

	}

	Thread.sleep(5000);

	for (int i = 0; i < 3; i++)
	{
	    System.out.println("***************************************************************");
	    System.out.println("Current State before call ===>>> " + circuitBreaker.getState());
	    Try.ofSupplier(decorateSupplier).recover(applicationResources::getFallbackResource);
	    System.out.println("Current State after call ===>>> " + circuitBreaker.getState());
	    System.out.println("***************************************************************");

	}

    }

}
